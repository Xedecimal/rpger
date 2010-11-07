package sdk.types;

/**
 * A small, editable rectangle.
 * @author Xedecimal
 */
public class RectD {
	public double x, y, w, h, r, b;

	public RectD()
	{
x = 0;
			y = 0;
			w = 0;
			h = 0;
			r = 0;
			b = 0;
		}

		public RectD(double x, double y, double w, double h)
		{
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.r = x+w;
			this.b = y+h;
		}

		public RectD(RectD r) { this(r.x, r.y, r.w, r.h); }

		public boolean Contains(int xp, int yp)
		{
			if (xp > x && xp < x+w && yp > y && yp < y+h) return true;
			return false;
		}

		public static boolean Contains(Rect r, int xp, int yp)
		{
			if (xp > r.X && xp < r.X+r.Width && yp > r.Y && yp < r.Y+r.Height) return true;
			else return false;
		}

		public void Offset(int xp, int yp)
		{
			x += xp;
			y += yp;
			r = x+w;
			b = y+h;
		}

		public RectD GetOffset(int xp, int yp)
		{
			return new RectD(x + xp, y+yp, w, h);
		}

		public double XMid() { return x + (w / 2); }

		public String ToString()
		{
			return String.format("Core.RectF {0}, {1}, {2}, {3}", x, y, w, h);
		}
}
