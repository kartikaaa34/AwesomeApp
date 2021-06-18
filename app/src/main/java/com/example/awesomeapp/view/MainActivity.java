package com.example.awesomeapp.view;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.awesomeapp.util.Constant;
import com.example.awesomeapp.util.PaginationScrollListener;
import com.example.awesomeapp.R;
import com.example.awesomeapp.adapter.RecyclerViewAdapter;
import com.example.awesomeapp.util.StartApplication;
import com.example.awesomeapp.model.Content;
import com.example.awesomeapp.model.Photo;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Menu menu;
    private RecyclerViewAdapter mAdapter;
    private ArrayList<Photo> contentArrayList = new ArrayList<>();
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    private int perPage = 10;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.main_progress)
    ProgressBar progressBar;
    @BindView(R.id.toolbar)
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        checkInternet();
        setUI(0);
    }

    private void checkInternet() {
        if (!StartApplication.checkInternetConnection(MainActivity.this)) {
            swipeRefresh.setRefreshing(false);
            rlEmpty.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    public void setUI(int option) {
        if (option == 0) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(gridLayoutManager);

            mAdapter = new RecyclerViewAdapter(contentArrayList, 0, this);
            recyclerView.setAdapter(mAdapter);

            swipeRefresh.setOnRefreshListener(() -> {
                checkInternet();
                currentPage = PAGE_START;
                isLastPage = false;
                swipeRefresh.setRefreshing(true);
                loadActivity();
            });

            recyclerView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {

                @Override
                protected void loadMoreItems() {
                    isLoading = true;
                    currentPage += 1;
                    new Handler().postDelayed(() -> loadActivityNext(), 1000);
                }

                @Override
                public int getTotalPageCount() {
                    return TOTAL_PAGES;
                }

                @Override
                public boolean isLastPage() {
                    return isLastPage;
                }

                @Override
                public boolean isLoading() {
                    return isLoading;
                }
            });

        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);

            mAdapter = new RecyclerViewAdapter(contentArrayList, 1, this);
            recyclerView.setAdapter(mAdapter);

            swipeRefresh.setOnRefreshListener(() -> {
                checkInternet();
                currentPage = PAGE_START;
                isLastPage = false;
                swipeRefresh.setRefreshing(true);
                loadActivity();
            });

            recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {

                @Override
                protected void loadMoreItems() {
                    isLoading = true;
                    currentPage += 1;
                    new Handler().postDelayed(() -> loadActivityNext(), 1000);
                }

                @Override
                public int getTotalPageCount() {
                    return TOTAL_PAGES;
                }

                @Override
                public boolean isLastPage() {
                    return isLastPage;
                }

                @Override
                public boolean isLoading() {
                    return isLoading;
                }
            });

        }

        loadActivity();

    }

    public void loadActivity() {
//        swipeRefresh.setRefreshing(true);

        Call<Content> call = Constant.service.loadContent("Bearer " + Constant.token, currentPage, perPage);
        call.enqueue(new Callback<Content>() {
            @Override
            public void onResponse(Call<Content> call, Response<Content> response) {
                contentArrayList.clear();
                rlEmpty.setVisibility(View.GONE);

                Content content = response.body();
                try {
                    if (content != null) {
                        progressBar.setVisibility(View.GONE);

                        for (Photo photo : content.getPhotos()) {
                            Photo p = new Photo(photo.getId(),
                                    photo.getWidth(),
                                    photo.getHeight(),
                                    photo.getUrl(),
                                    photo.getPhotographer(),
                                    photo.getPhotographerUrl(),
                                    photo.getPhotographerId(),
                                    photo.getAvgColor(),
                                    photo.getSrc(),
                                    photo.getLiked());
                            contentArrayList.add(p);
                        }

                        swipeRefresh.setRefreshing(false);
                        mAdapter.notifyDataSetChanged();

                        if (contentArrayList.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            rlEmpty.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            rlEmpty.setVisibility(View.GONE);
                        }

                        if (currentPage <= TOTAL_PAGES) {
                            if (!isLastPage) {
                                mAdapter.addLoadingFooter();
                            }
                        } else {
                            isLastPage = true;
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        swipeRefresh.setRefreshing(false);
                        recyclerView.setVisibility(View.GONE);
                        rlEmpty.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    swipeRefresh.setRefreshing(false);
                    recyclerView.setVisibility(View.GONE);
                    rlEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Content> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    public void loadActivityNext() {
        Call<Content> call = Constant.service.loadContent("Bearer " + Constant.token, currentPage, perPage);
        call.enqueue(new Callback<Content>() {
            @Override
            public void onResponse(Call<Content> call, Response<Content> response) {
                Content content = response.body();

                try {
                    mAdapter.removeLoadingFooter();
                    isLoading = false;

                    if (content != null) {
                        progressBar.setVisibility(View.GONE);
                        for (Photo photo : content.getPhotos()) {
                            Photo p = new Photo(photo.getId(),
                                    photo.getWidth(),
                                    photo.getHeight(),
                                    photo.getUrl(),
                                    photo.getPhotographer(),
                                    photo.getPhotographerUrl(),
                                    photo.getPhotographerId(),
                                    photo.getAvgColor(),
                                    photo.getSrc(),
                                    photo.getLiked());
                            contentArrayList.add(p);
                        }
                        mAdapter.notifyDataSetChanged();

                        if (contentArrayList.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            rlEmpty.setVisibility(View.VISIBLE);
                        }

                        if (currentPage <= TOTAL_PAGES) {
                            if (!isLastPage) {
                                mAdapter.addLoadingFooter();
                            }
                        } else {
                            isLastPage = true;
                        }
                    }
                } catch (Exception e) {
                    recyclerView.setVisibility(View.GONE);
                    rlEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Content> call, Throwable t) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    boolean refresh = false;

    @Override
    public void onResume() {
        super.onResume();
        if (refresh) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkInternet();
                    currentPage = PAGE_START;
                    isLastPage = false;
                    setUI(0);
                }
            }, 1000);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        refresh = true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_grid:
//                checkInternet();
                currentPage = PAGE_START;
                isLastPage = false;
                setUI(0);
                return true;
            case R.id.action_list:
//                checkInternet();
                currentPage = PAGE_START;
                isLastPage = false;
                setUI(1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}