package demo.entity;

import javax.xml.bind.annotation.XmlRootElement;

@Immutable
@XmlRootElement
public class SimpleId {

	public static final SimpleId empty = new SimpleId();

	public static SimpleId make(Integer id) {
		return new SimpleId(id);
	}

	/**
	 * this may be null, meaning that this entity hasn't had an id assigned to it yet.
	 */
	public final Integer id;

	private SimpleId() {
		id = null;
	}

	private SimpleId(Integer id) {
		this.id = id;
	}

	@Override public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }

		SimpleId simpleId = (SimpleId) o;

		return id != null ? id.equals(simpleId.id) : simpleId.id == null;

	}

	@Override public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override public String toString() {
		return "" + id;
	}

}
