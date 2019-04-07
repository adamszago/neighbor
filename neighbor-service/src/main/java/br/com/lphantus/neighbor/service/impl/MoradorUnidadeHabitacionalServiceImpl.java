package br.com.lphantus.neighbor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.MoradorUnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.entity.MoradorUnidadeHabitacional;
import br.com.lphantus.neighbor.entity.MoradorUnidadeHabitacionalPK;
import br.com.lphantus.neighbor.repository.IMoradorUnidadeHabitacionalDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IMoradorUnidadeHabitacionalService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class MoradorUnidadeHabitacionalServiceImpl extends GenericService<MoradorUnidadeHabitacionalPK, MoradorUnidadeHabitacionalDTO, MoradorUnidadeHabitacional> implements
		IMoradorUnidadeHabitacionalService {

	@Autowired
	private IMoradorUnidadeHabitacionalDAO moradorUnidadeHabitacionalDAO;

	@Override
	public List<MoradorUnidadeHabitacionalDTO> listarMoradoresCondominio(final CondominioDTO condominio, final Boolean somenteAtivos) throws ServiceException {
		try {
			return this.moradorUnidadeHabitacionalDAO.listarMoradoresCondominio(condominio, somenteAtivos);
		} catch (final DAOException e) {
			getLogger().debug("Erro ao listar moradores por condominio.", e);
			throw new ServiceException(e);
		}
	}

	@Override
	public void atualizarRelacionamento(final MoradorUnidadeHabitacionalDTO relacionamento, final MoradorDTO morador) throws ServiceException {
		MoradorUnidadeHabitacionalPK chave = new MoradorUnidadeHabitacionalPK();
		chave.setIdMorador(morador.getPessoa().getIdPessoa());
		chave.setIdUnidade(relacionamento.getUnidadeHabitacional().getIdUnidade());

		MoradorUnidadeHabitacional relacionamentoBanco = findById(chave);
		if (null != relacionamentoBanco) {
			boolean houveAlteracao = false;

			if (relacionamento.isAdimplente() != relacionamentoBanco.isAdimplente()) {
				relacionamentoBanco.setAdimplente(relacionamento.isAdimplente());
				houveAlteracao = true;
			}

			if ((relacionamentoBanco.getDataFim() != null && !relacionamentoBanco.getDataFim().equals(relacionamento.getDataFim()))
					|| (relacionamento.getDataFim() != null && !relacionamento.getDataFim().equals(relacionamentoBanco.getDataFim()))) {
				relacionamentoBanco.setDataFim(relacionamento.getDataFim());
				houveAlteracao = true;
			}

			if ((relacionamentoBanco.getDataInicio() != null && !relacionamentoBanco.getDataInicio().equals(relacionamento.getDataInicio()))
					|| (relacionamento.getDataInicio() != null && !relacionamento.getDataInicio().equals(relacionamentoBanco.getDataInicio()))) {
				relacionamentoBanco.setDataFim(relacionamento.getDataFim());
				houveAlteracao = true;
			}

			if (!relacionamentoBanco.getTipoMorador().equals(relacionamento.getTipoMorador())) {
				relacionamentoBanco.setTipoMorador(relacionamento.getTipoMorador());
				houveAlteracao = true;
			}

			if (houveAlteracao) {
				update(relacionamentoBanco);
			}
		}
	}

}