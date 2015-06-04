package long_bloomfilter;
public interface Factory<T> {
	public T create();
	public void destroy();
}
