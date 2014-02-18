/**
 * 
 * The CornerStore class is utilized to determine corners and store these in an
 * index.
 * 
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 1.0
 * 
 */
public class CornerStore {

	private Corner[] cArrary;
	private int cornerIndex;
	private int flag = 0;

	/**
	 * Contains constructor for the Array of corners
	 * 
	 * @param cArrary
	 */
	public CornerStore(Corner[] cArrary) {
		this.cArrary = cArrary;
	}

	/**
	 * Determines if a Corner exists, the for loop cycles through the cArray if
	 * a corner is found then i becomes the cornerIndex
	 * 
	 * @param x
	 * @param y
	 * @return true or false depending on corner existence
	 */
	public boolean isAnyCorner(float x, float y) {
		for (int i = 0; i < cArrary.length; i++) {
			if (cArrary[i].isCorner(x, y)) {
				cornerIndex = i;
				return true;
			}
		}
		return false;
	}

	/**
	 * The following are task to set the PassNumber in relevance to cornerIndex.
	 * At the beginning of this class the flag is initialized at 0 when the
	 * PassNumber from the Corner class is equal to 4 the flag will be set to 1,
	 * otherwise if the PassNumber equals 1 then the flag value will not be
	 * changed.
	 * 
	 * Should the flag equal 0 as well as the PassNumber be less than 4, then
	 * the cornerIndex with it's PassNumber will be incremented otherwise if the
	 * flag equals 0 and the PassNumber for the corner is greater than 1 we
	 * decrease the cornerIndex with it's PassNumber.
	 */
	public void setPassNumber() {
		if (getPassNumber() == 4) {
			flag = 1;
		} else if (getPassNumber() == 1) {
			flag = 0;
		}

		if (flag == 0 && getPassNumber() < 4) {
			cArrary[cornerIndex].increasePassNumber();
		} else if (flag == 1 && getPassNumber() > 1) {
			cArrary[cornerIndex].decreasePassNumber();
		}
	}

	/**
	 * Pass each corner
	 * 
	 * @return PassNumber relevant to cornerIndex (index of corners determined)
	 */
	public int getPassNumber() {
		return cArrary[cornerIndex].getPassNumber();
	}

	/**
	 * Flag to solve the dead loop
	 * 
	 * @return flag value
	 */
	public int getFlag() {
		return flag;
	}
}
