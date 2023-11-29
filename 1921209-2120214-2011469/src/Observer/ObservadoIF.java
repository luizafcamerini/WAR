package Observer;

public interface ObservadoIF {
	public void addObservador(ObservadorIF o);

	public void removeObservador(ObservadorIF o);

	public int get(int i);
}
