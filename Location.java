
/**
 * @author Brandon Domash
 * This class is used to store the location of objects, in the form (x,y)
 **/
public class Location
{
    //(a,b) corresponds to (x,y)
    private int a;
    private int b;
    
    /**
     * Default constructor that creates a new Location at the point (0,0); 
     */
    public Location()
    {
        a = 0;
        b = 0;
    }
    /**
     * Perameterized constructor that creates a new Location at the input points
     * @param ap The x coordinate of the Location
     * @param bp The y coordinate of the Location
     */
    public Location(int ap, int bp)
    {
        a = ap;
        b = bp;
    }
    /**
     * Sets the x coordinate of the Location
     * @param a The new x coordinate of the Location
     */
    public void setA(int a)
    {
        this.a = a;
    }
    /**
     * Sets the y coordinate of the Location
     * @param b The new y coordinate of the Location
     */
    public void setB(int b)
    {
        this.b = b;
    }
    /**
     * Returns the x coordinate of the Location
     * @return the x coordinate of the Location
     */
    public int getA()
    {
        return a;
    }
    /**
     * Returns the y coordinate of the Location
     * @return the y coordinate of the Location
     */
    public int getB()
    {
        return b;
    }
    /**
     * Determines whether or not two Locations are at the same points
     * @return true if the two Locations are equal
     */
    public boolean isEqual(Location l)
    {
        if(getA() == l.getA() && getB()==l.getB())
            return true;
        else 
            return false;
    }
}