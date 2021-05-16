package interfaces;

import com.example.parkingmanagment.BaseFragment;

public interface FragmentStakeCommunicator {
    public abstract BaseFragment getFragmentFromByIndexNumber(int index);
    public abstract void fragmentStackOverflowed();

    /**
     * @return boolean Return true if you want to continue onBackPressed or else return false if you don't
     */
    public abstract boolean fragmentStackUnderFlow();
}
