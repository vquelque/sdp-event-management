package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.AbstractShowcaseFragment;
import ch.epfl.sweng.eventmanager.users.Role;

/**
 * Allows an administrator a manage the users allowed to work with this event (= access to admin
 * features).
 */
public class EventUserManagementFragment extends AbstractShowcaseFragment {
    private static final String TAG = "UserManagement";

    private RecyclerView mUserList;
    private UserListAdapter mUserListAdapter;

    public EventUserManagementFragment() {
        // Required empty public constructor
        super(R.layout.fragment_event_user_management);
    }

    @Override
    public void onResume() {
        super.onResume();

        model.getEvent().observe(this, ev -> {
            if (ev == null) {
                Log.e(TAG, "Got null model from parent activity");
            }

            mUserListAdapter = new UserListAdapter(ev);
            mUserList.setAdapter(mUserListAdapter);

            return;
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Help text
        TextView helpText = view.findViewById(R.id.help_text);
        helpText.setText(R.string.user_management_help_text);

        // User lis header
        TextView left_header = view.findViewById(R.id.user_list_left_header);
        left_header.setText(getString(R.string.user_list_left_header));
        TextView right_header = view.findViewById(R.id.user_list_right_header);
        right_header.setText(getString(R.string.user_list_right_header));

        // User list
        mUserList = view.findViewById(R.id.user_list);
        mUserList.setHasFixedSize(true);
        RecyclerView.LayoutManager userListLayoutManager = new LinearLayoutManager(getActivity());
        mUserList.setLayoutManager(userListLayoutManager);

        // Add user/role form
        Spinner formSpinner = view.findViewById(R.id.form_spinner);
        String[] roles = Role.asArrayOfString();
        ArrayAdapter<String> formSpinnerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, roles);
        formSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        formSpinner.setAdapter(formSpinnerAdapter);

        Button formButton = view.findViewById(R.id.form_button);
        formButton.setText(getString(R.string.add_button));

        EditText formMail = view.findViewById(R.id.form_mail);
        formMail.setHint(getString(R.string.email_field));

        return view;
    }
}
