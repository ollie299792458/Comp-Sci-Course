package uk.ac.cam.cl.part1b.groupproject.echo.energybudget.fragments.tips;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Comparator;
import java.util.List;

import uk.ac.cam.cl.part1b.groupproject.echo.energybudget.R;

/**
 * Created by oliver on 13/02/18.
 *
 * Standard custom list adapter for java - extended to display two textviews (title and description), and a "done/undo" button
 *
 * Is dependent on {@link TipDatabase}
 */

public class CustomListAdapterTip extends ArrayAdapter<Tip> implements View.OnClickListener{

    /**
     * The current set of {@link Tip}
     */
    private final List<Tip> dataSet;
    /**
     * The context in which the adapter operates
     */
    private Context mContext;
    /**
     * Stores the default small text colour (to be restored on {@link #deShade(ViewHolder)})
     */
    private int oldDescColor;
    /**
     * Stores the default big text colour (to be restored on {@link #deShade(ViewHolder)})
     */
    private int oldTitleColor;

    /**
     * View lookup cache, allows views to be recycled without needing to reinflate
     */
    private static class ViewHolder {
        TextView tipTitle;
        TextView tipDescription;
        ImageButton tipDone;
    }

    /**
     * Creates a customlistadapter tip
     * @param data the data object to be operated on for the entirety of the adapters life (is final)
     * @param context the context in which the adapter is to operate
     */
    public CustomListAdapterTip(List<Tip> data, Context context) {
        super(context, R.layout.tips_custom_list_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    /**
     * Used to handle clicks to the done/undo button
     *
     * @param v The view that was clicked
     */
    @Override
    public void onClick(View v) {

        //get the list item that was clicked
        int position=(Integer) v.getTag();

        //switch statement to support more buttons/actions if necessary
        switch (v.getId())
        {
            //todo add undo toast
            case R.id.tips_done:
                //get the tips database - could be abstracted out, results in a triangle of dependencies - but works
                TipDatabase tipDatabase = TipDatabase.getInstance(mContext);
                //update the tip (alternating based on its current state, to handle both 'done' and 'undo' actions
                tipDatabase.updateTip(dataSet.get(position), !dataSet.get(position).isDone(), mContext);
                //update the list
                this.notifyDataSetChanged();
                break;
        }
    }

    /**
     * Sort data according {@link Tip#compareTo(Tip)}
     */
    private void sortDataSet() {
        dataSet.sort(Comparator.naturalOrder());
    }

    /**
     * Overridden to force the sorting of the dataset before continuing
     *
     * {@link ArrayAdapter#notifyDataSetChanged()}
     */
    @Override
    public void notifyDataSetChanged() {
        sortDataSet();
        super.notifyDataSetChanged();
    }


    /**
     * Called by android to create the listview item by item
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Tip tip = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        //sort dataset
        sortDataSet();

        //if the cachedview hasn't been inflated, inflate it
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.tips_custom_list_item, parent, false);
            //set up the cache
            viewHolder.tipTitle = convertView.findViewById(R.id.tip_title);
            viewHolder.tipDescription = convertView.findViewById(R.id.tip_description);
            viewHolder.tipDone = convertView.findViewById(R.id.tips_done);

            //store the default text colours
            oldDescColor = viewHolder.tipDescription.getCurrentTextColor();
            oldTitleColor = viewHolder.tipTitle.getCurrentTextColor();

            //put the cache into the tag
            convertView.setTag(viewHolder);
        } else {
            //otherwise, just get the cached view from the tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //TODO maybe add animation
        /*
        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;*/

        //fill in the view with the information in the tip
        viewHolder.tipTitle.setText(tip.getTitle());
        viewHolder.tipDescription.setText(tip.getDescription());
        viewHolder.tipDone.setOnClickListener(this);
        viewHolder.tipDone.setTag(position);
        viewHolder.tipDone.setEnabled(true);
        viewHolder.tipDone.setContentDescription(mContext.getString(R.string.done));
        viewHolder.tipDone.setImageResource(R.drawable.ic_done_tip);
        //make the 'done' button stand out
        viewHolder.tipDone.setBackgroundTintList(
                ColorStateList.valueOf(ContextCompat.getColor(mContext,R.color.colorAccent)));
        deShade(viewHolder);
        if (tip.isDone()) {
            viewHolder.tipDone.setContentDescription(mContext.getString(R.string.not_done));
            viewHolder.tipDone.setImageResource(R.drawable.ic_undo_tip);
            viewHolder.tipDone.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(mContext,R.color.greyed_out)));
            shade(viewHolder);
        }
        // Return the completed view to render on screen
        return convertView;
    }

    /**
     * Sets the text colour back to default for both the title and description
     * @param viewHolder viewholder to have text colour changed
     */
    private void deShade(ViewHolder viewHolder) {
        viewHolder.tipTitle.setTextColor(oldTitleColor);
        viewHolder.tipDescription.setTextColor(oldDescColor);
    }

    /**
     * Sets the text colour to a greyed out shade, for both the title and description
     * @param viewHolder viewholder to have text colour changed
     */
    private void shade(ViewHolder viewHolder) {
        viewHolder.tipTitle.setTextColor(mContext.getColor(R.color.greyed_out));
        viewHolder.tipDescription.setTextColor(mContext.getColor(R.color.greyed_out));
    }
}