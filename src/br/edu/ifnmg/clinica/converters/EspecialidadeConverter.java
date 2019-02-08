package br.edu.ifnmg.clinica.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.edu.ifnmg.clinica.model.Especialidade;

@FacesConverter(value = "especialidadeConverter", forClass = Especialidade.class)
public class EspecialidadeConverter implements Converter<Object>{

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		if(arg2 != null && !arg2.isEmpty()) {
			return (Especialidade) arg1.getAttributes().get(arg2);
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if(arg2 instanceof Especialidade) {
			Especialidade entity = (Especialidade) arg2;
			if(entity != null && entity instanceof Especialidade && entity.getId()!=null) {
				arg1.getAttributes().put(entity.getNome(), entity);
				return entity.getNome();
			}
		}
		return null;
	}

	

}
