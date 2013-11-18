package com.strongloop.android.loopback.guide.lessons;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.strongloop.android.loopback.Model;
import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.guide.GuideApplication;
import com.strongloop.android.loopback.guide.R;
import com.strongloop.android.loopback.guide.util.HtmlFragment;

/**
 * Implementation for Lesson Two: Existing Data? No Problem.
 */
public class LessonTwoFragment extends HtmlFragment {

    /**
     * Unlike Lesson One, our WeaponModel class is based _entirely_ on an existing schema.
     *
     * In this case, every field in Oracle that's defined as a NUMBER type becomes a Number,
     * and each field defined as a VARCHAR2 becomes a String.
     *
     * When we load these models from Oracle, LoopBack uses these property setters and getters
     * to know what data we care about. If we left off `extras`, for example, LoopBack would
     * simply omit that field.
     */
    public static class WeaponModel extends Model {
        String name;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        float audibleRange;
        public float getAudibleRange() {
            return audibleRange;
        }
        public void setAudibleRange(float audibleRange) {
            this.audibleRange = audibleRange;
        }

        float effectiveRange;
        public float getEffectiveRange() {
            return effectiveRange;
        }
        public void setEffectiveRange(float effectiveRange) {
            this.effectiveRange = effectiveRange;
        }

        int rounds;
        public int getRounds() {
            return rounds;
        }
        public void setRounds(int rounds) {
            this.rounds = rounds;
        }

        String extras;
        public String getExtras() {
            return extras;
        }
        public void setExtras(String extras) {
            this.extras = extras;
        }

        String fireModes;
        public String getFireModes() {
            return fireModes;
        }
        public void setFireModes(String fireModes) {
            this.fireModes = fireModes;
        }
    }

    /**
     * Our custom ModelRepository subclass. See Lesson One for more information.
     */
    public static class WeaponRepository extends ModelRepository<WeaponModel> {
        public WeaponRepository() {
            super("weapon", WeaponModel.class);
        }
    }

    /**
     * Loads all Weapon models from the server. To make full use of this, return to your (running)
     * Sample Application and restart it with the DB environment variable set to "oracle".
     * For example, on most *nix flavors (including Mac OS X), that looks like:
     *
     * 1. Stop the current server with Ctrl-C.
     * 2. DB=oracle slc run app
     *
     * What does this do, you ask? Without that environment variable, the Sample Application uses
     * simple, in-memory storage for all models. With the environment variable, it uses a custom-made
     * Oracle adapter with a demo Oracle database we host for this purpose. If you have existing
     * data, it's that easy to pull into LoopBack. No need to leave it behind.
     *
     * Advanced users: LoopBack supports multiple data sources simultaneously, albeit on a per-model
     * basis. In your next project, try connecting a schema-less model (e.g. our Ammo example)
     * to a Mongo data source, while connecting a legacy model (e.g. this Weapon example) to
     * an Oracle data source.
     */
    private void sendRequest() {
        // 1. Grab the shared RestAdapter instance.
        GuideApplication app = (GuideApplication)getActivity().getApplication();
        RestAdapter adapter = app.getLoopBackAdapter();

        // 2. Instantiate our WeaponRepository.See LessonOneView for further discussion.
        WeaponRepository repository = adapter.createRepository(WeaponRepository.class);

        // 3. Rather than instantiate a model directly like we did in Lesson One, we'll query
        //    the server for all Weapons, filling out our ListView with the results. In this case,
        //    the Repository is really the workhorse; the Model is just a simple container.

        repository.findAll(new ModelRepository.FindAllCallback<LessonTwoFragment.WeaponModel>() {
            @Override
            public void onSuccess(List<WeaponModel> models) {
                list.setAdapter(new WeaponListAdapter(getActivity(), models));
            }

            @Override
            public void onError(Throwable t) {
                Log.e(getTag(), "Cannot save Ammo model.", t);
                showResult("Failed.");
            }
        });
    }

    private void showResult(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Basic ListAdapter implementation using our custom Model type.
     */
    private static class WeaponListAdapter extends ArrayAdapter<WeaponModel> {
        public WeaponListAdapter(Context context, List<WeaponModel> list) {
            super(context, 0, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        android.R.layout.simple_list_item_1, null);
            }

            WeaponModel model = getItem(position);
            if (model == null) return convertView;

            TextView textView = (TextView)convertView.findViewById(
                    android.R.id.text1);
            textView.setText(
                    model.getName() + " - " + model.getEffectiveRange());

            return convertView;
        }
    }

    //
    // GUI glue
    //
    private ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setRootView((ViewGroup) inflater.inflate(
                R.layout.fragment_lesson_two, container, false));

        list = (ListView)getRootView().findViewById(R.id.list);

        setHtmlText(R.id.content, R.string.lessonTwo_content);

        installButtonClickHandler();

        return getRootView();
    }

    private void installButtonClickHandler() {
        final Button button = (Button) getRootView().findViewById(R.id.sendRequest);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendRequest();
            }
        });
    }
}
