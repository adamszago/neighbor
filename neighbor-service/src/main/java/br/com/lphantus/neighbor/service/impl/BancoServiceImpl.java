package br.com.lphantus.neighbor.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.BancoDTO;
import br.com.lphantus.neighbor.entity.Banco;
import br.com.lphantus.neighbor.repository.IBancoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IBancoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class BancoServiceImpl extends GenericService<Long, BancoDTO, Banco> implements
		IBancoService {

	@Autowired
	private IBancoDAO bancoDAO;

	@Override
	protected void updateValidate(final Banco entity) throws ServiceException {
		super.updateValidate(entity);
		if ((entity.getCodigoBanco() == null) || (entity.getCodigoBanco() <= 0)) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("BANCO_CODIGO_VAZIO"));
		}

		if (entity.getNomeBanco() == null) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("BANCO_NOME_VAZIO"));
		}

		if (entity.getSigla() == null) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("BANCO_SIGLA_VAZIO"));
		}

		if (entity.getCondominio() == null) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("BANCO_CONDOMINIO_VAZIO"));
		}
	}

	@Override
	protected void saveValidate(final Banco entity) throws ServiceException {
		super.saveValidate(entity);

		if ((entity.getCodigoBanco() == null) || (entity.getCodigoBanco() <= 0)) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("BANCO_CODIGO_VAZIO"));
		}

		if (entity.getNomeBanco() == null) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("BANCO_NOME_VAZIO"));
		}

		if (entity.getSigla() == null) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("BANCO_SIGLA_VAZIO"));
		}

		if (entity.getCondominio() == null) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("BANCO_CONDOMINIO_VAZIO"));
		}

		if (this.existeBanco(entity.getCodigoBanco())) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("BANCO_EXISTE"));
		}

	}

	@Override
	public void gravarBanco(final BancoDTO banco) throws ServiceException {
		final Banco entidade = BancoDTO.Builder.getInstance().createEntity(
				banco);
		save(entidade);

	}

	@Override
	public boolean existeBanco(final Long id) throws ServiceException {
		try {
			return this.bancoDAO.existeBanco(id);
		} catch (final DAOException exception) {
			getLogger().error("Erro ao buscar banco pelo ID informado.", exception);
			throw new ServiceException(
					"Erro ao buscar banco pelo ID informado.", exception);
		}

	}

	@Override
	public void alterarBanco(final BancoDTO banco) throws ServiceException {
		final Banco entidade = BancoDTO.Builder.getInstance().createEntity(
				banco);
		update(entidade);

	}

	@Override
	public Integer obtemSizeCarteira(final String codigoCompensacao) {

		// informacoes obtidas de http://www.boletobancario.com/docs/banks.html

		Integer retorno;
		if ((codigoCompensacao != null) && !codigoCompensacao.isEmpty()) {
			final Integer codigoCompensacaoNumerico = Integer
					.valueOf(codigoCompensacao);
			switch (codigoCompensacaoNumerico) {

			case 151: // nossa caixa
			case 89: // credisan
			case 756: // sicoob
				retorno = 1;
				break;
			case 422: // safra
			case 356: // banco real amro bank
			case 85: // cecred
			case 104:// caixa
			case 237: // bradesco
			case 755: // bank of america
			case 21: // banestes
			case 4: // banco do nordeste
				retorno = 2;
				break;
			case 33: // santander
			case 341: // itau
			case 745: // citibank
			case 399:// hsbc
			case 1:// banco do brasil
				retorno = 3;
				break;
			case 37:// banpara
				retorno = 4;
				break;
			case 3:// banco da amazonia: verificar com banco
			case 353:// banco banespa: verificar com banco
			case 291: // bcn: verificar com banco
			case 479: // bankboston: verificar com banco
			case 70: // brb (banco de brasilia): verificar com banco
			case 389: // mercantil do brasil: verificar com banco
			case 748: // sicredi: verificar com banco
			case 347: // sudameris: verificar com banco
			default:
				retorno = 0;
				break;
			}
		} else {
			retorno = BigDecimal.ZERO.intValue();
		}
		return retorno;
	}

	@Override
	public Integer obtemSizeNossoNumero(final String banco,
			final String carteira) {
		Integer retorno;

		final Integer numeroBanco = Integer.valueOf(banco);

		switch (numeroBanco) {
		// sicredi
		case 748:
			retorno = 5;
			break;
		// banco da amazonia e brb
		case 3:
		case 70:
			retorno = 6;
			break;
		// sicoob, banespa, banco do nordeste, bank of america, bcn,
		// bankboston, credisan, nossa caixa
		case 756:
		case 353:
		case 4:
		case 755:
		case 291:
		case 479:
		case 89:
		case 151:
			retorno = 7;
			break;
		// banestes, banpara, banrisul, sudameris
		case 21:
		case 37:
		case 41:
		case 347:
			retorno = 8;
			break;
		// cecred
		case 85:
			retorno = 9;
			break;
		// Mercantil do Brasil
		case 389:
			retorno = 10;
			break;
		// bradesco, citibank
		case 237:
		case 745:
			retorno = 11;
			break;
		// real
		case 356:
			retorno = 13;
			break;
		// caixa
		case 104:
			if (carteira.trim().equalsIgnoreCase("CR")
					|| carteira.trim().equalsIgnoreCase("SR")
					|| carteira.trim().equalsIgnoreCase("CS")) {
				// 14, 9 ou 8
				retorno = 14;
			} else {
				if (carteira.trim().equalsIgnoreCase("14")
						|| carteira.trim().equalsIgnoreCase("24")) {
					retorno = 15;
				} else {
					if (carteira.trim().equalsIgnoreCase("01")) {
						retorno = 17;
					} else {
						retorno = 0;
					}
				}
			}
			break;
		// hsbc
		case 399:
			if (carteira.trim().equalsIgnoreCase("CNR")) {
				retorno = 13;
			} else {
				if (carteira.trim().equalsIgnoreCase("CSB")) {
					retorno = 5;
				} else {
					retorno = 0;
				}
			}
			break;
		// santander
		case 33:
			if (carteira.trim().equalsIgnoreCase("CNR")
					|| carteira.trim().equalsIgnoreCase("CSR")
					|| carteira.trim().equals("101")
					|| carteira.trim().equalsIgnoreCase("102")) {
				retorno = 12;
			} else {
				final Integer nCarteira = Integer.valueOf(carteira);
				if (nCarteira.equals(1)) {
					retorno = 7;
				} else {
					retorno = 0;
				}
			}
			break;
		// banco do brasil
		case 1:
			final Integer numeroCarteira = Integer.valueOf(carteira);
			if (numeroCarteira.equals(16) || numeroCarteira.equals(18)) {
				retorno = 17;
			} else {
				retorno = 10;
			}
			break;
		// safra
		case 422:
			final List<Integer> carteirasSafra = Arrays.asList(new Integer[] {
					1, 2, 4 });
			final Integer numCarteira = Integer.valueOf(carteira);
			if (carteirasSafra.contains(numCarteira)) {
				retorno = 8;
			} else {
				if (numCarteira.equals(6)) {
					retorno = 17;
				} else {
					retorno = 0;
				}
			}
			break;
		default:
			retorno = 0;
			break;
		}

		return retorno;
	}
}
