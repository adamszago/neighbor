package br.com.lphantus.neighbor.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.lphantus.neighbor.entity.IEntity;

public abstract class DTOBuilder<T extends AbstractDTO, E> {
	
	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	public abstract T create(final E entity);

	public abstract E createEntity(final T outer);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final List<T> createList(final Collection<E> entidades) {
		final List<T> lista = new ArrayList<T>();
		final boolean entidadesNaoNulas = (entidades != null)
				&& !entidades.isEmpty();
		if (entidadesNaoNulas) {
			for (final E entidade : entidades) {
				lista.add((T) ((IEntity) entidade).createDto());
			}
		}
		return lista;
	}

	public final List<E> createListEntity(final Collection<T> dtos) {
		final List<E> lista = new ArrayList<E>();
		final boolean dtosNaoNulos = (dtos != null) && !dtos.isEmpty();
		if (dtosNaoNulos) {
			for (final T dto : dtos) {
				lista.add(createEntity(dto));
			}
		}
		return lista;
	}

}
