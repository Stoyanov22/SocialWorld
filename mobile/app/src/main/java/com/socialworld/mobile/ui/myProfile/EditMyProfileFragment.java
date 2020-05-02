package com.socialworld.mobile.ui.myProfile;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.socialworld.mobile.R;
import com.socialworld.mobile.entities.UserEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditMyProfileFragment extends Fragment {

    private MyProfileViewModel myProfileViewModel;
    private EditText editTextName;
//    private EditText editTextEmail;

    private Calendar birthCalendar;
    private EditText editDateBirth;

    private Spinner countrySpinner;

    public EditMyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myProfileViewModel = new ViewModelProvider(requireActivity()).get(MyProfileViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_my_profile, container, false);
        editDateBirth = view.findViewById(R.id.edit_date_of_birth);
        editTextName = view.findViewById(R.id.edit_text_name);
//        editTextEmail = view.findViewById(R.id.edit_text_email);
        countrySpinner = view.findViewById(R.id.edit_country_spinner);

        myProfileViewModel.getUserLiveData().observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity userEntity) {
                if (userEntity != null) {
                    if (userEntity.getName() != null) {
                        editTextName.setText(userEntity.getName());
                    }
                    if (userEntity.getName() != null) {
                        editTextName.setText(userEntity.getName());
                    }
                }
            }
        });

        birthCalendar = Calendar.getInstance();
        final String myFormat = "dd/MM/yyyy";
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        final DatePickerDialog.OnDateSetListener dateOfBirth = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                birthCalendar.set(Calendar.YEAR, year);
                birthCalendar.set(Calendar.MONTH, monthOfYear);
                birthCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editDateBirth.setText(sdf.format(birthCalendar.getTime()));
            }

        };

        editDateBirth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(requireActivity(), dateOfBirth, birthCalendar
                        .get(Calendar.YEAR), birthCalendar.get(Calendar.MONTH),
                        birthCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        List<String> countryOptions = new ArrayList<>();
        for (String country : Locale.getISOCountries()) {
            Locale locale = new Locale("en", country);
            countryOptions.add(locale.getDisplayCountry());
        }
        final ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, countryOptions);
        countrySpinner.setAdapter(countryAdapter);
//        if (prevCountry != null) {
//            countrySpinner.setSelection(countryAdapter.getPosition(prevCountry));
//        }

        return view;
    }
}
