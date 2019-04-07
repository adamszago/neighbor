package br.com.lphantus.neighbor.entity;

import java.io.Serializable;

import br.com.lphantus.neighbor.common.AbstractDTO;

/**
 * Interface representando as classes da aplicação.
 * 
 * @author Elias da Silva Policena { elias.policena@lphantus.com.br }
 * @since 14/10/2014
 *
 * @param <Chave>
 *            A classe da chave da entidade.
 */
public interface IEntity<Chave, T extends AbstractDTO> extends Serializable {

	/**
	 * Cria o DTO baseado na entidade.
	 * 
	 * @return Um objeto que extende a interface de DTOs.
	 */
	T createDto();

}
