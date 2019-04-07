package br.com.lphantus.neighbor.service.scheduled.manifold;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.entity.Totem;
import br.com.lphantus.neighbor.service.IMailManager;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.utils.Constantes.ConstantesJPA;

/**
 * Implementacao da pesquisa de totem duplicado.
 * 
 * @author elias.policena@lphantus.com.br
 * @since 23/02/2015
 *
 */
@Component
@Scope("singleton")
@Transactional(propagation = Propagation.SUPPORTS)
public class TotemDuplicadoVisitanteComponent implements ITotemDuplicadoVisitante {

	@PersistenceContext(unitName = ConstantesJPA.PERSISTENCE_UNIT)
	private EntityManager manager;

	@Autowired
	private IMailManager mailManager;

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS)
	public void process() throws ServiceException {
		List<Totem> listaDuplicadosVisitante = manager.createNamedQuery("Totem.duplicadoVisitante").getResultList();

		if (null != listaDuplicadosVisitante && !listaDuplicadosVisitante.isEmpty()) {
			mailManager.enviarEmailTotemDuplicado(listaDuplicadosVisitante, 3);
		}
	}

}
