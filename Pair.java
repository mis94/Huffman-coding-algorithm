package M;

public class Pair<L,R> implements Comparable<Pair>
{
    public L l; // attribute l ---> left
    public R r; // attribute r ---> right
    public Pair(L l, R r)
    {
        this.l = l;
        this.r = r;
    }
    public L getL()
    { 
    	return l; 
    }
    public R getR()
    { 
    	return r; 
    }
    public void setL(L l)
    { 
    	this.l = l; 
    }
    public void setR(R r)
    { 
    	this.r = r; 
    }
    
    @Override
    public int compareTo(Pair o) {
    	/* when we compare between two objects from this class this method specifies the criteria */
        return (int)l < (int)o.l ? -1 : (int)l > (int)o.l ? 1 : 0;
        /* returns -1 if the sent object is bigger or one if I'm bigger than the sent or zero if equal */
    }
}
