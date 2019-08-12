package com.selfmade.gradiamut.myplan;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.selfmade.gradiamut.myplan.Util.Model;
import com.selfmade.gradiamut.myplan.Util.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class PlanFragment extends Fragment {
    View v;
    public FloatingActionButton plan_create_fab;
    private DatabaseReference mRoot;
    private DatabaseReference mRootUser;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mCurrentUser;
    public String user_School_id, user_title, spinnerItem;

    private RecyclerView mRecycleView;
    private Spinner mSpinner;
    private LinearLayout linearLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.plan_fragment, container, false);

        mDatabase = FirebaseDatabase.getInstance();
        mRootUser = mDatabase.getReference().child("user");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mRecycleView = v.findViewById(R.id.plan_fragment_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecycleView.setLayoutManager(layoutManager);
        linearLayout = v.findViewById(R.id.faitchier);

        plan_create_fab = v.findViewById(R.id.plan_fragment_fab);



        plan_create_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), CreatePlan.class);
                startActivity(i);
            }
        });



        return v;

    }

    @Override
    public void onStart() {
        super.onStart();


        mRootUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                user_School_id = (String) dataSnapshot.child(mCurrentUser.getUid()).child("school_id").getValue();

                user_title = (String) dataSnapshot.child(mCurrentUser.getUid()).child("title").getValue();

                if (user_title.equals("director")) {
                    plan_create_fab.setVisibility(View.INVISIBLE);

                    mRoot = mDatabase.getReference().child("plan").child(user_School_id).child("subject");
                    mSpinner = v.findViewById(R.id.auth_spinner);
                    final List<String> author_name_list = new ArrayList<String>();

                    for (DataSnapshot topicSnapshot : dataSnapshot.getChildren()) {
                        String author_name = (String) topicSnapshot.child("name").getValue();

                        author_name_list.add(author_name);
                        Log.d("tp_id", "onDataChange: " + author_name_list);



                    }

                    String tp_id = (String) dataSnapshot.child("author").getKey();
                    Log.d("tp_id", "onDataChange: " + tp_id);

                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>
                            (getContext(), android.R.layout.simple_spinner_item, author_name_list);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    mSpinner.setAdapter(spinnerAdapter);
                    Log.w("spinnerAda", spinnerAdapter.toString());

                    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            spinnerItem = (String) parent.getItemAtPosition(position);
                            Log.d("SpinnerItem", "onDataChange: " + spinnerItem);

                            final Query query = mRoot.orderByChild("author").equalTo(spinnerItem);

                            // Get the id of the selected spinner
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        final String key = child.getKey();
                                        Log.d("StackOverFlow", key);


                                    }

                                    Log.w("DbRoot", mRoot.toString());


                                    FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter =
                                            new FirebaseRecyclerAdapter<Model, ViewHolder>(
                                                    Model.class,
                                                    R.layout.plan_row,
                                                    ViewHolder.class,
                                                    query
                                            ) {

                                                @Override
                                                protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
                                                    final String postKey = getRef(position).getKey();

                                                    viewHolder.setSubject_img(getContext(), model.getSubject_img());
                                                    viewHolder.setSubject_name("Subject : " + model.getSubject_name());
                                                    viewHolder.setAuthor("Auhtor : " + model.getAuthor());
                                                    viewHolder.setDays("Days : " + model.getDays());
                                                    viewHolder.setYear_group("Academic Year : " + model.getYear_group());
                                                    viewHolder.setStd_num("Number Of Student : " + model.getStd_num());

                                                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            Intent singlePlan = new Intent(getContext(), SinglePlan.class);
                                                            singlePlan.putExtra("itemId", postKey);
                                                            singlePlan.putExtra("schoolId", user_School_id);
                                                            singlePlan.putExtra("userTitle", user_title);
                                                            startActivity(singlePlan);


                                                        }
                                                    });


                                                }
                                            };

                                    mRecycleView.setAdapter(firebaseRecyclerAdapter);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


//                    mRoot.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            mSpinner = v.findViewById(R.id.auth_spinner);
//                            final List<String> author_name_list = new ArrayList<String>();
//
//                            for (DataSnapshot topicSnapshot : dataSnapshot.getChildren()) {
//                                String author_name = (String) topicSnapshot.child("author").getValue();
//
//                                author_name_list.add(author_name);
//                                Log.d("tp_id", "onDataChange: " + author_name_list);
//
//
//
//                            }
//
//                            String tp_id = (String) dataSnapshot.child("author").getKey();
//                            Log.d("tp_id", "onDataChange: " + tp_id);
//
//                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>
//                                    (getContext(), android.R.layout.simple_spinner_item, author_name_list);
//                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
//                            mSpinner.setAdapter(spinnerAdapter);
//                            Log.w("spinnerAda", spinnerAdapter.toString());
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });




                }
                else if (user_title.equals("teacher")) {

                    Log.w("Title and school ID", user_title + user_School_id);

                    plan_create_fab.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);


                    mRoot = mDatabase.getReference().child("plan").child(user_School_id).child("subject");

                    //Query query = mRoot.orderByChild("topic_name").equalTo(spinnerItem);


                    mRoot.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String id = dataSnapshot.getKey();
                            Log.w("id", id);
                            Query query = mRoot.orderByChild("userId").equalTo(mCurrentUser.getUid());

//                            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    for (DataSnapshot inChild : dataSnapshot.getChildren()) {
//                                        final String key = inChild.getKey();
//                                        Log.d("inChild", key);
//                                        if (key.equals(mCurrentUser.getUid())){
//
//                                        }
//                                        }
//
//                                    }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });

                            Log.d("inChild", query.toString());

                            FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter =
                                    new FirebaseRecyclerAdapter<Model, ViewHolder>(
                                            Model.class,
                                            R.layout.plan_row,
                                            ViewHolder.class,
                                            mRoot
                                    ) {

                                        @Override
                                        protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
                                            final String postKey = getRef(position).getKey();

                                            viewHolder.setSubject_img(getContext(), model.getSubject_img());
                                            viewHolder.setSubject_name("Subject : " + model.getSubject_name());
                                            viewHolder.setAuthor("Auhtor : " + model.getAuthor());
                                            viewHolder.setDays("Days : " + model.getDays());
                                            viewHolder.setYear_group("Academic Year : " + model.getYear_group());
                                            viewHolder.setStd_num("Number Of Student : " + model.getStd_num());

                                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent singlePlan = new Intent(getContext(), SinglePlan.class);
                                                    singlePlan.putExtra("itemId", postKey);
                                                    singlePlan.putExtra("schoolId", user_School_id);
                                                    singlePlan.putExtra("userTitle", user_title);
                                                    startActivity(singlePlan);

                                                }
                                            });


                                        }
                                    };

                            mRecycleView.setAdapter(firebaseRecyclerAdapter);


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("mRootFirebaseError", databaseError.getMessage());
            }
        });


    }


}
