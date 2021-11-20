package com.example.moviedb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.Movies;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static String ID = MainActivity.class.getSimpleName();
    public static String TITLE = "title";
    public static String RELEASE_DATE = "1111.11.11";
    public static String RATING = "1";
    public static String LANG = "undefined";
    public static String DESC = "lorem";
    public static String VOTE = "0";
    public static String POP = "90";

    public ArrayList<HashMap<String,String>> movieList = new ArrayList<>();

    private final String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    JSONObject movieJSON = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        lv = findViewById(R.id.list);
        new GetMovies().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.contextAbout) {
            startActivity(new Intent(this, activity_about.class));
        }
        return true;
    }

    public void passData(View view){
        Intent intent = new Intent(this, activity_detail.class);

        TextView textid = findViewById(R.id.movie_id);
        String idtext = textid.getText().toString();
        try {
            JSONObject movieJSON = new JSONObject(JSONparser());
            JSONArray movies = movieJSON.getJSONArray("results");
            for (int i = 0; i < movies.length(); i++){
                JSONObject m = movies.getJSONObject(i);

                String jsid = m.getString("id");
                String title = m.getString("title");
                String date = m.getString("release_date");
                String overview = m.getString("overview");
                String rating = m.getString("vote_average");
                String lang = m.getString("original_language");
                String vote = m.getString("vote_count");
                String pop = m.getString("popularity");

                HashMap<String, String> movieMap = new HashMap<>();
                movieMap.put("id", jsid);
                movieMap.put("title", title);
                movieMap.put("date", date);
                movieMap.put("overview", overview);
                movieMap.put("rating", rating);
                movieMap.put("language", lang);
                movieMap.put("vote", vote);
                movieMap.put("pop", pop);

                if (idtext.equals(jsid)){
                    intent.putExtra(ID, movieMap.get("id"));
                    intent.putExtra(TITLE, movieMap.get("title"));
                    intent.putExtra(RELEASE_DATE, movieMap.get("date"));
                    intent.putExtra(RATING, movieMap.get("rating"));
                    intent.putExtra(LANG, movieMap.get("language"));
                    intent.putExtra(DESC, movieMap.get("overview"));
                    intent.putExtra(VOTE, movieMap.get("vote"));
                    intent.putExtra(POP, movieMap.get("pop"));
                    break;
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        startActivity(intent);
    }

    public String JSONparser() {
        InputStream is = getResources().openRawResource(R.raw.movies);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return writer.toString();
    }


    private class GetMovies extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... arg0){
            if(movieJSON != null){
                try {
                    JSONObject movieJSON = new JSONObject(JSONparser());
                    JSONArray movies = movieJSON.getJSONArray("results");
                    for(int i = 0; i < movies.length(); i++){
                        JSONObject m = movies.getJSONObject(i);

//                        //Using Model
//                        Movies movie = new Movies();
//
//                        movie.setTitle(m.getString("id"));
//                        movie.setTitle(m.getString("title"));
//                        movie.setReleaseDate(m.getString("release_date"));
//                        movie.setOverview(m.getString("overview"));
//                        movie.setVoteAverage(m.getDouble("vote_average"));
//                        movie.setOriginalLanguage(m.getString("original_language"));
//                        movie.setPosterPath("https://themoviedb.org/t/p/w500" + m.getString("poster_path"));
//
//                        moviesModel.add(movie);

                        String id = m.getString("id");
                        String title = m.getString("title");
                        String date = m.getString("release_date");
                        String overview = m.getString("overview");
                        String rating = m.getString("vote_average");
                        String lang = m.getString("original_language");
                        String poster_path = "https://themoviedb.org/t/p/w500" + m.getString("poster_path");

                        HashMap<String, String> movieMap = new HashMap<>();
                        movieMap.put("id", id);
                        movieMap.put("title", title);
                        movieMap.put("date", date);
                        movieMap.put("overview", overview);
                        movieMap.put("rating", rating);
                        movieMap.put("language", lang);
                        movieMap.put("poster", poster_path);

                        movieList.add(movieMap);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(),
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show());
                }
            }
            else{
                Log.e(TAG, "Failed to parse JSON!");
                runOnUiThread(() -> Toast.makeText(getApplicationContext(),
                        "JSON might be empty",
                        Toast.LENGTH_LONG).show());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);

            SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, movieList,
                    R.layout.activity_movie_list, new String[]{"id","title", "overview", "date"},
                    new int[]{R.id.movie_id,R.id.movie_title, R.id.movie_desc, R.id.release_date});
            lv.setAdapter(adapter);
//            for (int i = 0; i < adapter.getCount(); i++) {
//                HashMap<String, Object> hm = (HashMap<String, Object>) adapter.getItem(i);
//                String imgUrl = (String) hm.get("poster");
//
//                ImageLoader imageLoaderTask = new ImageLoader();
//
//                HashMap<String, Object> hmDownload = new HashMap<String, Object>();
//                hm.put("flag_path", imgUrl);
//                hm.put("position", i);
//
//                // Starting ImageLoaderTask to download and populate image in the listview
//                imageLoaderTask.execute(hm);
//            }
        }
        private class ImageLoader extends AsyncTask<HashMap<String, Object>, Void, HashMap<String, Object>>{

            @Override
            protected HashMap<String, Object> doInBackground(HashMap<String, Object>... hm) {
                InputStream is;
                String imgUrl = (String) hm[0].get("flag_path");
                int position = (int) hm[0].get("position");
                URL url;

                try {
                    url = new URL(imgUrl);
                    HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
                    urlConnect.connect();

                    is = urlConnect.getInputStream();

                    // Getting Caching directory
                    File cacheDirectory = getBaseContext().getCacheDir();

                    // Temporary file to store the downloaded image
                    File tmpFile = new File(cacheDirectory.getPath() + "/wpta_"+position+".png");

                    FileOutputStream fOutStream = new FileOutputStream(tmpFile);

                    Bitmap b = BitmapFactory.decodeStream(is);

                    // Writing the bitmap to the temporary file as png or jpeg file
                    b.compress(Bitmap.CompressFormat.JPEG,10, fOutStream);

                    // Flush the FileOutputStream
                    fOutStream.flush();

                    //Close the FileOutputStream
                    fOutStream.close();

                    HashMap<String, Object> hmBitmap = new HashMap<String, Object>();

                    hmBitmap.put("image", tmpFile.getPath());

                    return hmBitmap;

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(@NonNull HashMap<String, Object> result) {
                // Getting the path to the downloaded image
                String path = (String) result.get("image");

                // Getting the position of the downloaded image
                int position = (Integer) result.get("position");

                // Getting adapter of the listview
                SimpleAdapter adapter = (SimpleAdapter) lv.getAdapter();

                // Getting the hashmap object at the specified position of the listview
                HashMap<String, Object> hm = (HashMap<String, Object>) adapter.getItem(position);

                // Overwriting the existing path in the adapter
                hm.put("flag_path", path);

                // Noticing listview about the dataset changes
                adapter.notifyDataSetChanged();

            }
        }
    }

    public static class AdapterMovieList extends RecyclerView.Adapter<AdapterMovieList.ViewHolder>{
        Context context;
        List<Movies> moviesList;

        public AdapterMovieList(Context context, List<Movies> moviesList){
            this.context = context;
            this.moviesList = moviesList;
        }

        @NonNull
        @Override
        public AdapterMovieList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemview = LayoutInflater.from(context).inflate(R.layout.activity_movie_list,null);
            return new ViewHolder(itemview);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterMovieList.ViewHolder holder, int position) {
            String thumbnailUrl = moviesList.get(position).getPosterPath();
            Picasso.with(context).load(thumbnailUrl).fit().centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.photos);
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }
        public static class ViewHolder extends RecyclerView.ViewHolder{
            ImageView photos;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                photos = itemView.findViewById(R.id.movie_image);
            }
        }
    }
}
