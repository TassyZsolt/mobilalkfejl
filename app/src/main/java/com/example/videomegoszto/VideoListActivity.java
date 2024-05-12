package com.example.videomegoszto;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity {

    private static final String LOG_TAG = VideoListActivity.class.getName();
    private FirebaseUser user;

    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private List<Video> videoList;

    @SuppressLint("MissingInflatedId")

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video_list);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Log.d(LOG_TAG,"Hitelesített Felhasználó!");
        }else{
            Log.d(LOG_TAG,"Nem Hitelesített Felhasználó!");
            finish();
        }
        setupVideoView(R.id.videoView1,"android.resource://" + getPackageName() + "/" + R.raw.morning);
        setupVideoView(R.id.videoView2,"android.resource://" + getPackageName() + "/" + R.raw.traffic);


        recyclerView = findViewById(R.id.videoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        videoList = getVideos(); //
        adapter = new VideoAdapter(videoList);
        recyclerView.setAdapter(adapter);

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Videos");



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void setupVideoView(int videoViewId, String videoPath){
        VideoView videoView = findViewById(videoViewId);
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!videoView.isPlaying()) {
                    videoView.start();
                } else {
                    videoView.pause();
                }
            }
        });
    }

    private List<Video> getVideos() {
        List<Video> videos = new ArrayList<>();
        String path1 = "android.resource://" + getPackageName() + "/" + R.raw.morning;
        videos.add(new Video("Jó reggelt", path1));

        String path2 = "android.resource://" + getPackageName() + "/" + R.raw.traffic;
        videos.add(new Video("Forgalom", path2));
        return videos;
    }

    private class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

        private List<Video> videos;

        VideoAdapter(List<Video> videos) {
            this.videos = videos;
        }

        @Override
        public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_list, parent, false);
            return new VideoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(VideoViewHolder holder, int position) {
            Video video = videos.get(position);
           // holder.title.setText(video.getTitle());

        }

        @Override
        public int getItemCount() {
            return videos.size();
        }

        class VideoViewHolder extends RecyclerView.ViewHolder {
            TextView title;

            VideoViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.videoTitle); // feltételezve, hogy van egy TextView az items_list.xml-ben
            }
        }
    }

    private class Video {
       // private String title;
        private String url;

        Video(String title, String url) {
           // this.title = title;
            this.url = url;
        }

//        //public String getTitle() {
//            return title;
//        }

        public String getUrl() {
            return url;
        }
    }
}
