package com.example.examiner.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examiner.Model.ProjectModel;
import com.example.examiner.R;

import java.util.ArrayList;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.ViewHolder> {
    Context context;
    ArrayList<ProjectModel> list;

    public TeacherAdapter(Context context, ArrayList<ProjectModel> list) {
        this.context = context;
        this.list = list;
    }
    public void clearData() {
        list.clear();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TeacherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherAdapter.ViewHolder holder, int position) {

        ProjectModel projectModel = list.get(position);
        holder.studentnameAndActivity.setText(projectModel.getAction());
        holder.time.setText(projectModel.getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentnameAndActivity, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentnameAndActivity = itemView.findViewById(R.id.item_student_activity);
            time = itemView.findViewById(R.id.item_time);
        }
    }
}