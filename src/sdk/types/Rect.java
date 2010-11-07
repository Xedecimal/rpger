/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sdk.types;

/**
 * A simple every day 2d Cartesian rectangle.
 * @author Xedecimal
 */
public class Rect {
	/** The horizontal position of this Rect. */
	public int X;
	/** The vertical position of this Rect. */
public int Y;
	/** The width of this Rect. */
	public int Width;
	/** The height of this Rect. */
	public int Height;
	/** The horizontal coordinate of the right side of this Rect. */
	public int right() { return X + Width; }
	/** The vertical coordinate of the bottom of this Rect. */
	public int bottom() { return Y + Height; }

	/** Initialize all values to 0. */
	public Rect()
	{
			X = 0;
			Y = 0;
			Width = 0;
			Height = 0;
		}

	/**
	 * Initialize this Rect with the specified coordinates.
	 * @param x Horizontal location.
	 * @param y Vertical location.
	 * @param w Width.
	 * @param h Height.
	 */
	public Rect(int x, int y, int w, int h)
	{
		this.X = x;
		this.Y = y;
		this.Width = w;
		this.Height = h;
	}

	/**
	 * Create this Rect from a copy of another Rect.
	 * @param r The source Rect.
	 */
	public Rect(Rect r) { this(r.X, r.Y, r.Width, r.Height); }

	public String ToString()
	{
		return "Rect {X=" + X + ", Y=" + Y + ", W=" + Width + ", H=" + Height + "}";
	}

	/// <summary>
	/// Returns true if the specified coordinates are within the bounds of this Rect.
	/// </summary>
	/// <param name="xp">The horizontal coordinate.</param>
	/// <param name="yp">The vertical coordinate.</param>
	/// <returns>True if inside, otherwise false.</returns>
	public boolean Contains(int xp, int yp)
	{
		if (xp > X && xp < X+Width && yp > Y && yp < Y+Height) return true;
		return false;
	}

	/// <summary>
	/// Returns true if the specified coordiantes are within the bounds of specified Rect.
	/// </summary>
	/// <param name="r">The Rect to check.</param>
	/// <param name="xp">The horizontal coordinate.</param>
	/// <param name="yp">The vertical coordinate.</param>
	/// <returns></returns>
	public static boolean Contains(Rect r, int xp, int yp)
	{
		if (xp > r.X && xp < r.X+r.Width && yp > r.Y && yp < r.Y+r.Height) return true;
		else return false;
	}

	/// <summary>
	/// Offsets the entire Rect to a new location.
	/// </summary>
	/// <param name="xp">Horizontal offset.</param>
	/// <param name="yp">Vertical offset.</param>
	public void Offset(int xp, int yp)
	{
		X += xp; Y += yp;
	}

	/// <summary>
	/// Return a new rectangle with the new offset coordinates.
	/// </summary>
	/// <param name="xp">Horizontal offset.</param>
	/// <param name="yp">Vertical offset.</param>
	/// <returns></returns>
	public Rect GetOffset(int xp, int yp)
	{
		return new Rect(X + xp, Y + yp, Width, Height);
	}

	public void Resize(int nw, int nh)
	{
		Width = X - nw;
		Height = Y - nh;
	}
}
