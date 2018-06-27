package com.example.android.newsapp2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by egi-megi on 07.06.18.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(Activity context, ArrayList<Article> articles) {
        // Here is initializing the ArrayAdapter's internal storage for the context and the list.
        super(context, 0, articles);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        final Article currentArticle = getItem(position);

        // Go to webpage with article if someone click on list'item
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentArticle.getWebUrl()));
                getContext().startActivity(intent);
            }
        };
        listItemView.setOnClickListener(listener);

        // Find the TextView in the list_item.xml layout with the ID title_text_view
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title_text_view);
        titleTextView.setText(currentArticle.getTitle());

        // Find the TextView in the list_item.xml layout with the ID section_name_text_view
        TextView sectionNameTextView = (TextView) listItemView.findViewById(R.id.section_name_text_view);
        sectionNameTextView.setText(currentArticle.getSectionName());

        //Set color of text cointainer
        View textContainer = listItemView.findViewById(R.id.single_list_item);
        if (position % 2 == 0) {
            textContainer.setBackgroundColor(getContext().getResources().getColor(R.color.even));
        } else {
            textContainer.setBackgroundColor(getContext().getResources().getColor(R.color.odd));
        }

        // Find the TextView in the list_item.xml layout with the ID date_text_view and show only date without time
        int indexT = currentArticle.getPublicationDate().indexOf("T");
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        dateTextView.setText(currentArticle.getPublicationDate().substring(0, indexT));

        // Find the TextView in the list_item.xml layout with the ID author_text_view
        TextView authorsTextView = (TextView) listItemView.findViewById(R.id.author_text_view);
        authorsTextView.setText(currentArticle.getAuthors());

        return listItemView;
    }


}

