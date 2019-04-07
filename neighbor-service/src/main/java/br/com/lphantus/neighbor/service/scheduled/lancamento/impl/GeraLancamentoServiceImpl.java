package br.com.lphantus.neighbor.service.scheduled.lancamento.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.entity.Lancamento;
import br.com.lphantus.neighbor.entity.LancamentoTipo;
import br.com.lphantus.neighbor.entity.TemplateLancamento;
import br.com.lphantus.neighbor.service.IGeraLancamentoService;
import br.com.lphantus.neighbor.service.IPessoaFisicaService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.utils.Constantes.ConstantesJPA;

@Service
@Scope("singleton")
@Transactional(propagation = Propagation.SUPPORTS)
public class GeraLancamentoServiceImpl implements IGeraLancamentoService {

	@PersistenceContext(unitName = ConstantesJPA.PERSISTENCE_UNIT)
	private EntityManager manager;

	@Autowired
	private IPessoaFisicaService pessoaFisicaService;

	@Override
	public void gerarLancamentos(final TemplateLancamento registro)
			throws ServiceException {

		final LancamentoTipo tipoEntrada = this.manager.find(
				LancamentoTipo.class, 1L);
		final Date dataCadastro = new Date();

		final List<MoradorDTO> moradores = this.pessoaFisicaService
				.buscarResponsaveisFinanceiros(registro.getCasas(), registro
						.getBlocos(), registro.getCondominio().createDto());
		for (final MoradorDTO morador : moradores) {
			final Lancamento novo = new Lancamento();
			novo.setTipoLancamento(tipoEntrada);
			novo.setAtivo(Boolean.TRUE);
			novo.setCentroCusto(registro.getCentroCusto());
			novo.setCondominio(registro.getCondominio());
			novo.setData(registro.getParamRepeticao());
			novo.setDataCadastro(dataCadastro);
			novo.setExcluido(Boolean.FALSE);
			novo.setNome(registro.getNome());
			novo.setObs(registro.getObs());
			novo.setPessoa(MoradorDTO.Builder.getInstance().createEntity(
					morador));
			novo.setValor(registro.getValor());
			this.manager.persist(novo);
		}
	}

}
