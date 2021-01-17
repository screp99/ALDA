package aufgabe4;

import java.util.Objects;

public class TelKnoten {

	public final int x;
	public final int y;

	public TelKnoten(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof TelKnoten) {
			TelKnoten n = (TelKnoten) o;
			if (this.x == n.x && this.y == n.y) {
				return true;
			}
		}
		return false;
	}

}
