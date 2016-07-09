import jcolibri.connectors.TypeAdaptor;


public class DemandaContratadaEnum implements TypeAdaptor {

	String _content;

	@Override
	public String toString() {
		return _content;
	}
	
	@Override
	public void fromString(String content) throws Exception {
		_content = content;
	}

}
