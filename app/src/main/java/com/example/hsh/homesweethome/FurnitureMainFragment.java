package com.example.hsh.homesweethome;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.hsh.homesweethome.Models.CategoryFurniture;
import com.example.hsh.homesweethome.Models.Furniture;
import com.example.hsh.homesweethome.network.APIService;
import com.example.hsh.homesweethome.network.Utils;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FurnitureMainFragment extends Fragment {

    private RecyclerViewAdapterMain verticalAdapter;
    private RecyclerView verticalRecyclerView;
    private APIService mAPIService;

    private ArrayList<CategoryFurniture> categories = new ArrayList<>();

    private String TAG  = "MainFragment";

    private PublishSubject<String> publishSubject = PublishSubject.create();
    private CompositeDisposable disposable = new CompositeDisposable();

    public FurnitureMainFragment() {
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.furniture_main_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAPIService = Utils.getAPIService();
        Call<List<Furniture>> call = mAPIService.getFurnitures();

        verticalRecyclerView = view.findViewById(R.id.recyclerview_id);
        verticalAdapter = new RecyclerViewAdapterMain(getActivity(), categories, new ArrayList<>());

        verticalRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        verticalRecyclerView.setAdapter(verticalAdapter);

        call.enqueue(new Callback<List<Furniture>>() {
            @Override
            public void onResponse(Call<List<Furniture>> call, Response<List<Furniture>> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, response.body().toString());
                    HashMap<String, ArrayList<Furniture>> splitFurnitures = new HashMap<>();
                    for(Furniture furniture : response.body()) {
                        if (splitFurnitures.get(furniture.getFurnitureCategory()) != null) {
                            splitFurnitures.get(furniture.getFurnitureCategory()).add(furniture);
                        } else {
                            ArrayList<Furniture> furnitures = new ArrayList<>();
                            furnitures.add(furniture);
                            splitFurnitures.put(furniture.getFurnitureCategory(), furnitures);
                        }
                    }

                    for (String furnitureCategory : splitFurnitures.keySet()) {
                        categories.add(new CategoryFurniture(furnitureCategory, splitFurnitures.get(furnitureCategory)));
                    }

                    verticalAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Furniture>> call, Throwable t) {
                Log.e(TAG, t.getMessage());

            }
        });

        //SearchView to be tied to backend
//        mSearchView = findViewById(R.id.search_bar);
//
//        mSearchView.getQuery();
//
//        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
//            @Override
//            public void onSearchTextChanged(String oldQuery, String newQuery) {
//
//            }
//        });
//
//        mSearchView.setOnLeftMenuClickListener(new FloatingSearchView.OnLeftMenuClickListener() {
//            @Override
//            public void onMenuOpened() {
//
//            }
//
//            @Override
//            public void onMenuClosed() {
//
//            }
//        });
//
//        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
//            @Override
//            public void onSearchTextChanged(String oldQuery, String newQuery) {
//
//            }
//        });



//        DisposableObserver<List<Furniture>> observer = getSearchObserver();
//
//        disposable.add(
//                publishSubject
//                        .debounce(300, TimeUnit.MILLISECONDS)
//                        .distinctUntilChanged()
//                        .switchMapSingle(new Function<String, Single<List<Furniture>>>() {
//                            @Override
//                            public Single<List<Furniture>> apply(String s) throws Exception {
//                                return mAPIService.getQueryFurnitures(20, s)
//                                        .subscribeOn(Schedulers.io())
//                                        .observeOn(AndroidSchedulers.mainThread());
//                            }
//                        })
//                        .subscribeWith(observer));
//
//        disposable.add(RxTextView.textChangeEvents(mSearchView)
//                .skipInitialValue()
//                .debounce(300, TimeUnit.MILLISECONDS)
//                /*.filter(new Predicate<TextViewTextChangeEvent>() {
//                    @Override
//                    public boolean test(TextViewTextChangeEvent textViewTextChangeEvent) throws Exception {
//                        return TextUtils.isEmpty(textViewTextChangeEvent.text().toString()) || textViewTextChangeEvent.text().toString().length() > 2;
//                    }
//                })*/
//                .distinctUntilChanged()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(searchContacts()));
//
//        disposable.add(observer);

    }


    private DisposableObserver<TextViewTextChangeEvent> searchContacts() {
        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                Log.d(TAG, "Search query: " + textViewTextChangeEvent.text());
                verticalAdapter.getFilter().filter(textViewTextChangeEvent.text());
                publishSubject.onNext(textViewTextChangeEvent.text().toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }


    private DisposableObserver<List<Furniture>> getSearchObserver() {
        return new DisposableObserver<List<Furniture>>() {
            @Override
            public void onNext(List<Furniture> furnitures) {
                furnitures.addAll(furnitures);
                verticalAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }


}