package utils;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.parkingmanagment.BaseActivity;
import com.example.parkingmanagment.BaseFragment;

import custom_view.PmButton;
import interfaces.FragmentStakeCommunicator;
import interfaces.OnFragmentChangeListener;


public class PmFragmentManager {

    FragmentManager fragmentManager;

    int frameId;

    FragmentStack fragmentStack;

    public PmFragmentManager(BaseActivity activity, int frameId, FragmentManager fragmentManager) {

        if (null == fragmentManager) {
            this.fragmentManager = activity.getSupportFragmentManager();
        }

        this.frameId = frameId;
    }

    public FragmentStack getFragmentStack(int fragmentStackCount, FragmentStakeCommunicator fragmentStakeCommunicator, boolean callGetFragmentEveryTime) {
        fragmentStack = new FragmentStack(fragmentStackCount, fragmentStakeCommunicator, callGetFragmentEveryTime);
        return fragmentStack;
    }

    public BaseFragment getCurrentFragmentByFrameId(int id) {
        return (BaseFragment) fragmentManager.findFragmentById(id);
    }

    public void populateFragment(BaseFragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameId, fragment, tag);
        if (null != fragmentStack && !fragmentStack.isFirstFragment()) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commit();
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }


    public class FragmentStack {

        BaseFragment[]            fragmentList;
        FragmentStakeCommunicator fragmentStakeCommunicator;
        int                       currentFragmentIndex = -1;
        boolean                   callGetFragmentEveryTime;
        private OnFragmentChangeListener onFragmentChangeListener;

        public FragmentStack(int fragmentStackCount, FragmentStakeCommunicator fragmentStakeCommunicator, boolean callGetFragmentEveryTime) {
            this.fragmentStakeCommunicator = fragmentStakeCommunicator;
            this.callGetFragmentEveryTime  = callGetFragmentEveryTime;
            fragmentList                   = new BaseFragment[fragmentStackCount];
        }

        private void updateFragmentStack(int index) {
            BaseFragment fragment = null;
            if (callGetFragmentEveryTime || fragmentList[index] == null) {
                fragment = fragmentStakeCommunicator.getFragmentFromByIndexNumber(index);

                if (null == fragment) {
                    return;
                }

                fragmentList[index] = fragment;

            } else {
                fragment = fragmentList[index];
            }

            populateFragment(fragment, fragment.getClass().toString());
        }

        public int getCurrentFragmentIndex() {
            return currentFragmentIndex;
        }

        public boolean isFirstFragment() {
            return currentFragmentIndex == 0;
        }

        public boolean isLastFragment() {
            return currentFragmentIndex == fragmentList.length - 1;
        }


        public void populateNextFragment() {
            if (isLastFragment()) {
                fragmentStakeCommunicator.fragmentStackOverflowed();
                return;
            }
            updateFragmentStack(++currentFragmentIndex);
            if (null != onFragmentChangeListener) {onFragmentChangeListener.fragmentChanged(currentFragmentIndex);}
        }

        public void popBackStack() {

            BaseActivity baseActivity = (BaseActivity) fragmentStakeCommunicator;
            if (isFirstFragment()) {
                boolean _continue = fragmentStakeCommunicator.fragmentStackUnderFlow();
                if (!_continue) {
                    return;
                }
            }
            baseActivity.superOnBackPressed();
            currentFragmentIndex--;
            if (null != onFragmentChangeListener) {onFragmentChangeListener.fragmentChanged(currentFragmentIndex);}
        }

        public void setBackButton(PmButton backButton) {
            backButton.setOnClickListener(view -> {
                popBackStack();
            });
        }

        public void setNextButton(PmButton nextButton) {
            nextButton.setOnClickListener(view -> {
                populateNextFragment();
            });
        }

        public BaseFragment getFragment(int index) {
            return fragmentList[index];
        }

        public BaseFragment[] getFragments() {
            return fragmentList;
        }

        public void setOnFragmentChangeListener(OnFragmentChangeListener onFragmentChangeListener){
            this.onFragmentChangeListener = onFragmentChangeListener;

        }
    }

}
