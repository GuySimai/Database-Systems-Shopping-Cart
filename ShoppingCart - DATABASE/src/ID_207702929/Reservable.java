package ID_207702929;

public interface Reservable {

	void reserve(int quantity, int id) throws ProductQuantityNotAvailableException;
	
}
