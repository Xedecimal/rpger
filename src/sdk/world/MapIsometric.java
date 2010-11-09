package sdk.world;

import java.io.IOException;
import java.nio.ByteBuffer;
import sdk.Engine;
import sdk.types.RPoint;
import sdk.types.RectD;
import sdk.types.RegionSet;
import sdk.types.Space;

/**
 *
 * @author Xedecimal
 */
public class MapIsometric
{
	/** Width of map measured in tiles. */
	public int Width;
	/** Height of map measured in tiles. */
	public int Height;

	/** Actual tile data. */
	public Tile[] Tiles;

	public int TileWidth = 64;
	public int TileHeight = 64;

	public int XTO;
	public int YTO;

	//TODO: Handle this elsewhere.
	public RPoint Marker;

	public Tile get(int x, int y) { return Tiles[x + (y * Width)]; }
	public Tile get(int x, int y, int z) { return Tiles[x + (y * Width)].GetStack(z); }

	public MapIsometric(Space parent, int width, int height)
	{
		Marker = new RPoint();
		Width = width;
		Height = height;

		Tiles = new Tile[width * height];
		int id = 0;
		for (int ix = 0; ix < width; ix++)
		{
			for (int iy = 0; iy < height; iy++)
			{
				//Tiles[id] = new Tile(parent, (ushort)Engine.r.Next(5), false);
				Tiles[id] = new Tile(parent, (short)0, false);
				Tiles[id].x = ix * 32;
				Tiles[id++].y = iy * 16;
			}
		}
	}

	public MapIsometric(Space space, int tileWidth, int tileHeight, int width, int height)
	{
		this(space, width, height);
		TileWidth = tileWidth;
		TileHeight = tileHeight;

		XTO = TileWidth / 2;
		YTO = TileHeight / 4;
	}

	public MapIsometric(Space space, ByteBuffer bb) throws IOException
	{
		Marker = new RPoint();

		XTO = TileWidth / 2;
		YTO = TileHeight / 4;

		Width = bb.getInt();
		Height = bb.getInt();
		Tiles = new Tile[Width*Height];
		System.out.println("Loaded map, Width (" + Width + ") Height (" + Height + ")");
		for (int ix = 0; ix < Width; ix++)
		{
			for (int iy = 0; iy < Height; iy++)
			{
				int count = bb.getInt();
				Tile t = new Tile(space, bb);
				Tiles[ix+(Width * iy)] = t;
			}
		}
	}

	public void Serialize(ByteBuffer bb) throws IOException
	{
		bb.putInt(Width);
		bb.putInt(Height);
		for (int ix = 0; ix < Width * Height; ix++)
		{
			Tile t = Tiles[ix];
			t.Serialize(bb);
			int count = Tiles[ix].count();
			for (int iy = 0; iy < count; iy++)
			{
				t = t.next;
				t.Serialize(bb);
			}
		}
	}

	public void generate()
	{
		for (int ix = 0; ix < Width; ix++)
			for (int iy = 0; iy < Height; iy++)
				this.get(ix, iy).ID = (short)Engine.r.nextInt(5);
	}

	/**
	 * Do not use GL translating because the tile needs to know where it is
	 * for interaction with the world (creating an emitter on destruction, etc)
	 * @param xoff Horizontal camera offset
	 * @param yoff Vertical camera offset
	 * @param RS RegionSet
	 */
	public void Render(int xoff, int yoff, RegionSet RS)
	{
		//Gl.glLoadIdentity();
		int left = (xoff / TileWidth);
		int right = (xoff / TileWidth) + (Engine.intMain.Width / TileWidth) + 2;
		int top = (yoff / YTO);
		int bottom = (yoff / YTO) + (Engine.intMain.Height / YTO) + 2;

		//Why the : ? for x/yoff here?
		float xstart = xoff < 0 ? -xoff : -xoff % TileWidth;
		RectD r = new RectD(
			xstart,
			yoff < 0 ? -yoff : -yoff % YTO, TileWidth, TileHeight); //Translated rectangle.

		double zOrder1 = Math.max(0, top) * .01;

		for (int iy = top; iy < bottom; iy++)
		{
			if (iy > Height-1) break;
			if (iy < 0) continue;

			r.Offset(-((iy % 2) * XTO), 0);

			for (int ix = left; ix < right; ix++)
			{
				if (ix > Width-1) break;
				if (ix < 0) continue;
				int id = ix + (iy * Width);
				Tile t = Tiles[id];

				t.Draw(RS, r, zOrder1);
				if (Marker.x == ix && Marker.y == iy) RS.Draw(2, r, zOrder1);

				r.Offset(TileWidth, 0);
			}
			r.x = xstart;
			r.Offset(0, YTO);
			zOrder1 += 0.01;
		}
	}

	public boolean InMap(int tx, int ty)
	{
		return tx >= 0 && ty >= 0 && tx < Width && ty < Height;
	}
}