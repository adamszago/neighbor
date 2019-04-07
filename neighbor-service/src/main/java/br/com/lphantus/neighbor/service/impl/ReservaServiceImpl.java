package br.com.lphantus.neighbor.service.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ConfiguracaoCondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.ReservaDTO;
import br.com.lphantus.neighbor.common.ReservaTempDTO;
import br.com.lphantus.neighbor.entity.Reserva;
import br.com.lphantus.neighbor.repository.IReservaDAO;
import br.com.lphantus.neighbor.repository.IReservaTempDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IConfiguracaoCondominioService;
import br.com.lphantus.neighbor.service.IMoradorService;
import br.com.lphantus.neighbor.service.IReservaService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class ReservaServiceImpl extends GenericService<Long, ReservaDTO, Reserva> implements IReservaService {

	@Autowired
	private IConfiguracaoCondominioService configuracaoCondominioService;

	@Autowired
	private IMoradorService moradorService;

	@Autowired
	private IReservaDAO reservaDAO;

	@Autowired
	private IReservaTempDAO reservaTempDAO;

	@Override
	public void validarReserva(final ReservaDTO reserva) throws ServiceException {
		try {

			if (this.reservaDAO.verificaDisponibilidade(reserva) != null) {
				throw new ServiceException("Item já está reservado!");
			}

			if (this.reservaDAO.verificarCarencia(reserva) != null) {
				throw new ServiceException(String.format("Morador já fez reserva em período inferior a %d dias!", reserva.getItemReserva().getCarenciaDiasReserva()));
			}

			if (reserva.getItemReserva() == null) {
				throw new ServiceException(GerenciadorMensagem.getMensagem("SELECIONE_ITEM_RESERVA"));
			}

			if (reserva.getMorador() == null) {
				throw new ServiceException(GerenciadorMensagem.getMensagem("SELECIONE_MORADOR"));
			}

		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public boolean itemReservaEstaPendenteAprovacaoNaData(final ReservaDTO reserva) throws ServiceException {
		try {
			return this.reservaTempDAO.itemReservaEstaPendenteAprovacaoNaData(reserva.getItemReserva(), reserva.getDataReserva());
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public void validarReservaAprovacao(final ReservaDTO reserva) throws ServiceException {
		try {
			if (this.reservaTempDAO.itemReservaEstaPendenteMorador(reserva.getItemReserva(), reserva.getMorador())) {
				throw new ServiceException(GerenciadorMensagem.getMensagem("RESERVA_JA_EXISTE_PENDENTE_MORADOR"));
			}

			ConfiguracaoCondominioDTO config = configuracaoCondominioService.buscarPorCondominio(reserva.getCondominio());

			if (null != config && null != config.getQtdeDiasMinimoReserva() && 0 != config.getQtdeDiasMinimoReserva()) {
				final Calendar dataAtual = Calendar.getInstance(); // data
																	// atual;
				final Calendar dataMinimaReserva = dataAtual;
				dataMinimaReserva.add(Calendar.DAY_OF_MONTH, config.getQtdeDiasMinimoReserva());

				// Verificar data minima para fazer reserva.
				if (reserva.getDataReserva().before(dataMinimaReserva.getTime())) {
					throw new ServiceException(GerenciadorMensagem.getMensagem("RESERVA_DATA_MINIMA_NOK"));
				}
			}

		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public void save(final Reserva reserva) throws ServiceException {
		try {
			validarReserva(reserva.createDto());
			this.reservaDAO.save(reserva);

		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public void save(final ReservaDTO reserva, final boolean solicitarAprovacao) throws ServiceException {
		try {
			validarReserva(reserva);

			if (solicitarAprovacao) {
				validarReservaAprovacao(reserva);
				this.reservaTempDAO.save(ReservaTempDTO.Builder.getInstance().createEntity(converterReservaParaReservaTemp(reserva)));
			} else {
				this.reservaDAO.save(ReservaDTO.Builder.getInstance().createEntity(reserva));
			}

		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<ReservaDTO> listarReservaPorMorador(final MoradorDTO morador, final CondominioDTO condominio) throws ServiceException {
		try {
			List<ReservaDTO> lista = this.reservaDAO.listarReservaPorMorador(morador, condominio);
			for (ReservaDTO item : lista) {
				if (null != item.getMorador()) {
					item.setMorador(moradorService.buscarDetalhesMorador(item.getMorador()));
				}
			}
			return lista;
		} catch (final Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public ReservaTempDTO converterReservaParaReservaTemp(final ReservaDTO res) {
		final ReservaTempDTO resTemp = new ReservaTempDTO();

		resTemp.setCondominio(res.getCondominio());
		resTemp.setDataReserva(res.getDataReserva());
		resTemp.setDataSolicitacao(res.getDataSolicitacao());
		resTemp.setItemReserva(res.getItemReserva());
		resTemp.setMorador(res.getMorador());
		resTemp.setPago(res.isPago());

		return resTemp;
	}

	@Override
	public List<ReservaTempDTO> getReservasPendenteAprovacao(CondominioDTO condominio) throws ServiceException {
		try {
			return reservaTempDAO.buscaReservasPendentesAprovacao(condominio);

		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public void deletarReservaPendente(final ReservaTempDTO res) throws ServiceException {
		try {
			if ((res == null) | (res.getId() == null)) {
				throw new ServiceException("Erro ao reprovar reserva - parametro null");
			} else {
				this.reservaTempDAO.delete(ReservaTempDTO.Builder.getInstance().createEntity(res));
			}
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<ReservaDTO> listarReservaPorCondominio(final CondominioDTO condominio) throws ServiceException {
		try {
			List<ReservaDTO> lista = this.reservaDAO.listarReservaPorCondominio(condominio);
			for (ReservaDTO item : lista) {
				if (null != item.getMorador()) {
					item.setMorador(moradorService.buscarDetalhesMorador(item.getMorador()));
				}
			}
			return lista;
		} catch (final DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}
}
