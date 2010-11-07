package sdk.types;

/**
 *
 * @author Xedecimal
 */
public class ImpactResult
{
	public ImpactType type;
	public Entity target;

	public ImpactResult(ImpactType type, Entity obj)
	{
		this.type = type;
		this.target = obj;
	}

	public ImpactResult(ImpactType type) { this(type, null); }
}