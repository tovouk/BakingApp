package com.josehinojo.bakingapp.Recipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.josehinojo.bakingapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.MyViewHolder>{
    ArrayList<ParcelableRecipe> recipeList;
    Context context;
    final private ListItemClickListener itemClickListener;

    public interface ListItemClickListener{
        void onListItemClick(ParcelableRecipe recipe);
    }

    public RecipeListAdapter(ArrayList<ParcelableRecipe> recipeList,Context context,ListItemClickListener listItemClickListener){
        this.recipeList = recipeList;
        this.context = context;
        this.itemClickListener = listItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView recipeName;
        public MyViewHolder(View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            itemClickListener.onListItemClick(recipeList.get(position));
        }
    }

    @NonNull
    @Override
    public RecipeListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListAdapter.MyViewHolder holder, int position) {
        ParcelableRecipe recipe = this.recipeList.get(position);
        Log.i("name",recipe.getName());
        holder.recipeName.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }
}
