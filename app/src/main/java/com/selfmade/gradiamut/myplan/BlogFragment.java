package com.selfmade.gradiamut.myplan;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
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

public class BlogFragment extends Fragment {
    View v;

    private DatabaseReference mRoot;
    private FirebaseDatabase mDatabase;
    private FloatingActionButton mNewBlog;
    // private DatabaseReference mRootUser;

//    private FirebaseUser mCurrentUser;
//    public String user_School_id, user_title;

    private RecyclerView mRecycleView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.blog_grament, container, false);
        mDatabase = FirebaseDatabase.getInstance();
        //mRootUser = mDatabase.getReference().child("user");
        //mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mNewBlog = v.findViewById(R.id.blog_fragment_fab);

        mNewBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createBlog = new Intent(getContext(), createBlog.class);
                startActivity(createBlog);
            }
        });


        mRecycleView = v.findViewById(R.id.blog_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecycleView.setLayoutManager(layoutManager);

        return v;

    }

    @Override
    public void onStart() {
        super.onStart();

        mRoot = mDatabase.getReference().child("blog");

        final FirebaseRecyclerAdapter<Model,ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.blog_row,
                        ViewHolder.class,
                        mRoot
                ) {
            @Override
            protected void populateViewHolder(final ViewHolder viewHolder, final Model model, int position) {
                final String postId = getRef(position).getKey();

               final DatabaseReference commentDb = mDatabase.getReference().child("blogComment").child(postId);

               commentDb.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       String commentNum = String.valueOf(dataSnapshot.getChildrenCount());
                       viewHolder.setTitle(model.getTitle());
                       viewHolder.setCreated_date(model.getCreated_date());
                       viewHolder.setContent(model.getContent());
                       viewHolder.setImg(getContext(), model.getImg());
                       viewHolder.setCommentNum(commentNum);

                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });



               //Retrieve Comment for the blog
                viewHolder.mCommentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LayoutInflater li = LayoutInflater.from(getContext());
                        View promptsView = li.inflate(R.layout.bring_comment, null);
                        View promptsView2 = li.inflate(R.layout.comment_recycler_row, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

                        alertDialogBuilder.setView(promptsView);

                        final RecyclerView commentR = promptsView.findViewById(R.id.comment_recycler);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        layoutManager.setReverseLayout(true);
                        layoutManager.setStackFromEnd(true);
                        commentR.setLayoutManager(layoutManager);

                        FirebaseRecyclerAdapter<Model, ViewHolder> commentRecyclerAdapter =
                                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                                        Model.class,
                                        R.layout.comment_recycler_row,
                                        ViewHolder.class,
                                        commentDb
                                ) {
                            @Override
                            protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
                                viewHolder.setName(model.getName());
                                viewHolder.setCommentContent(model.getCommentContent());
                            }
                        };
                        commentR.setAdapter(commentRecyclerAdapter);
                        alertDialogBuilder.setCancelable(true);

                        //alertDialogBuilder.
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();



                    }
                });


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent singleBlog = new Intent(getContext(), SingleBlog.class);
                        singleBlog.putExtra("itemId", postId);
                        startActivity(singleBlog);

                        //Toast.makeText(getContext(), postId, Toast.LENGTH_LONG).show();

                    }
                });

            }
        };
        mRecycleView.setAdapter(firebaseRecyclerAdapter);

    }


}
