package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CarteiraDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MovimentacaoDTO;
import br.com.lphantus.neighbor.entity.Carteira;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface ICarteiraDAO extends IGenericDAO<Carteira> {

	List<CarteiraDTO> buscarPorParametros(CondominioDTO condominio,
			Boolean ativo) throws DAOException;

	void atualizarCaixa(MovimentacaoDTO movimentacaoDTO, final boolean ehEntrada)
			throws DAOException;

	void atualizaBoleto(CarteiraDTO carteira) throws DAOException;

}