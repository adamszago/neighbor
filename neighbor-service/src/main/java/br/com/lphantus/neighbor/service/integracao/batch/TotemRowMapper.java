package br.com.lphantus.neighbor.service.integracao.batch;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import br.com.lphantus.neighbor.enums.EnumTempoAcesso;
import br.com.lphantus.neighbor.utils.Constantes;
import br.com.lphantus.neighbor.utils.Utilitarios;

/**
 * Classe que transforma um registro da base em uma instancia temporaria, para
 * utilizacao pelo spring batch.
 * 
 * @author elias.policena@lphantus.com.br
 * @since 26/11/2014
 *
 */
public class TotemRowMapper implements RowMapper<TotemTO> {

	@Override
	public TotemTO mapRow(final ResultSet resultSet, final int rowNum)
			throws SQLException {
		final TotemTO retorno = new TotemTO();

		retorno.setAtivo(resultSet.getBoolean("ATIVO"));
		retorno.setIdentificacao(resultSet.getString("IDENTIFICACAO"));
		retorno.setTipoTotem(resultSet.getLong("TIPO_TOTEM"));
		retorno.setIdPessoa(resultSet.getLong("ID_PESSOA"));
		retorno.setIdTotem(resultSet.getLong("ID_TOTEM"));
		retorno.setNome(resultSet.getString("NOME"));
		retorno.setSenha(resultSet.getString("SENHA"));

		// somente no novo sql
		int columnCount = resultSet.getMetaData().getColumnCount();
		if (columnCount > 7) {
			retorno.setTipoCancela(resultSet.getString("TIPO_CANCELA"));
			if (columnCount >= 10) {
				retorno.setTipoAcesso(resultSet.getLong("TIPO_ACESSO"));
				if (retorno.getTipoAcesso()
						.equals(EnumTempoAcesso.PERIODO_DIAS)) {
					retorno.setDataInicial(Utilitarios
							.formatarDiaHora(resultSet
									.getTimestamp("DATA_INICIAL")));
					retorno.setDataFinal(Utilitarios.formatarDiaHora(resultSet
							.getTimestamp("DATA_FINAL")));
				} else {
					retorno.setDataInicial(Constantes.VAZIO);
					retorno.setDataFinal(Constantes.VAZIO);
				}
			}
		}

		return retorno;
	}

}
