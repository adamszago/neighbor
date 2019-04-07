package br.com.lphantus.neighbor.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.DuplicataDTO;
import br.com.lphantus.neighbor.common.DuplicataParcelaDTO;
import br.com.lphantus.neighbor.common.FaturaDTO;
import br.com.lphantus.neighbor.entity.Duplicata;
import br.com.lphantus.neighbor.repository.IDuplicataDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IDuplicataService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class DuplicataServiceImpl extends GenericService<Long, DuplicataDTO, Duplicata> implements IDuplicataService {

	@Autowired
	private IDuplicataDAO duplicataDAO;

	@Override
	protected void saveValidate(final Duplicata entity) throws ServiceException {
		super.saveValidate(entity);
		if (entity == null) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("DUPLICATA_PARAMETRO_NULO"));
		}

		if ((entity.getFatura() == null) || (entity.getFatura().getId() == null)) {
			throw new ServiceException(GerenciadorMensagem.getMensagem("DUPLICATA_FATURA_VAZIA"));
		}

	}

	@Override
	protected void updateValidate(final Duplicata entity) throws ServiceException {
		super.updateValidate(entity);
		saveValidate(entity);
	}

	private void preencheQuitacaoTotalPago(final List<DuplicataDTO> duplicatas) {
		if ((null != duplicatas) && !duplicatas.isEmpty()) {
			for (final DuplicataDTO item : duplicatas) {
				if ((item.getParcelas() != null) && !item.getParcelas().isEmpty()) {
					BigDecimal soma = BigDecimal.ZERO;
					for (final DuplicataParcelaDTO parcela : item.getParcelas()) {
						if (parcela.isQuitada()) {
							soma = soma.add(parcela.getValor());
						}
					}
					item.setValorPago(soma);
					if ((null != item.getFatura()) && soma.equals(item.getFatura().getValor())) {
						item.setAberto(false);
					}
				}
			}
		}
	}

	@Override
	public List<DuplicataDTO> buscarPorCondominio(final CondominioDTO condominio) throws ServiceException {
		try {
			final List<DuplicataDTO> duplicatas = this.duplicataDAO.buscarPorCondominio(condominio);
			preencheQuitacaoTotalPago(duplicatas);
			return duplicatas;
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar duplicatas.", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<DuplicataDTO> buscarAtivasPorFatura(final FaturaDTO item) throws ServiceException {
		try {
			final List<DuplicataDTO> duplicatas = this.duplicataDAO.buscarAtivasPorFatura(item);
			preencheQuitacaoTotalPago(duplicatas);
			return duplicatas;
		} catch (final DAOException e) {
			getLogger().error("Erro ao buscar duplicatas.", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

}