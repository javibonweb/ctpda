package es.juntadeandalucia.ctpda.gestionpdt.web.converter;

public abstract class BaseConverter<T> {

    public T getAsObject(final String value) {
        if (value == null || value.length() == 0) {
            return null;
        }

        final Long id;
        try{
        	id = Long.parseLong(value);
        } catch(final NumberFormatException nfe){
        	nfe.printStackTrace();
        	return null;
        }

        return this.getFromService(id);
    }

    protected abstract T getFromService(Long id);
}
