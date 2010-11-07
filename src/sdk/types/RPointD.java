/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sdk.types;

/**
 *
 * @author Xedecimal
 */
public class RPointD {
	public double x = 0, y = 0;
	public RPointD() {}
	public RPointD(double tx, double ty) { x = tx; y = ty; }
	public RPointD Offset(double tx, double ty) { return new RPointD(x + tx, y + ty); }
	public String ToString() { return x + ", " + y; }
}
