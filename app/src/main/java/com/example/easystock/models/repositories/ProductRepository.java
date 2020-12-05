package com.example.easystock.models.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.easystock.listeners.GetSimilarProductsListener;
import com.example.easystock.models.Product;
import com.example.easystock.models.dao.ProductDao;
import com.example.easystock.models.db.AppDatabase;

import java.util.List;

public class ProductRepository {

    private ProductDao mProductDao;
    private LiveData<List<Product>> mAllProducts;

    private LiveData<List<Product>> mPriceAscFilter;
    private LiveData<List<Product>> mPriceDescFilter;
    private LiveData<List<Product>> mStockAscFilter;
    private LiveData<List<Product>> mStockDescFilter;
    private LiveData<List<Product>> mSizeAscFilter;
    private LiveData<List<Product>> mSizeDescFilter;

    public ProductRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mProductDao = db.mProductDao();
        mAllProducts = mProductDao.getAllProducts();

        mPriceAscFilter = mProductDao.getAscPriceFilter();
        mPriceDescFilter = mProductDao.getDescPriceFilter();
        mStockAscFilter = mProductDao.getAscStockFilter();
        mStockDescFilter = mProductDao.getDescStockFilter();
        mSizeAscFilter = mProductDao.getAscSizeFilter();
        mSizeDescFilter = mProductDao.getDescSizeFilter();
    }

    public LiveData<List<Product>> getAllProducts() {
        return mAllProducts;
    }

    /*Filtros de productos*/

    public LiveData<List<Product>> getProductFilter(String orderBy, String filter) {
        if (orderBy.equals("ASC")) {
            switch (filter.toUpperCase()) {
                case "PRICE":
                    return mPriceAscFilter;
                case "STOCK":
                    return mStockAscFilter;
                case "SIZE":
                    return mSizeAscFilter;
            }
        } else {
            switch (filter.toUpperCase()) {
                case "PRICE":
                    return mPriceDescFilter;
                case "STOCK":
                    return mStockDescFilter;
                case "SIZE":
                    return mSizeDescFilter;
            }
        }
        return mPriceAscFilter;
    }

    public void insertProduct(Product product) {
        new insertAsyncTask(mProductDao).execute(product);
    }

    public void updateProduct(Product product) {
        new UpdateProductAsyncTask(mProductDao).execute(product);
    }

    public void deleteProduct(Product product) {
        new DeleteProductAsyncTask(mProductDao).execute(product);
    }

    private static class insertAsyncTask extends AsyncTask<Product, Void, Void> {

        private ProductDao mAsyncTaskDao;

        insertAsyncTask(ProductDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Product... params) {
            mAsyncTaskDao.insertProduct(params[0]);
            return null;
        }
    }

    private class UpdateProductAsyncTask extends AsyncTask<Product, Void, Void> {

        private ProductDao mAsyncTaskDao;

        public UpdateProductAsyncTask(ProductDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Product... params) {
            mAsyncTaskDao.updateProduct(params[0]);
            return null;
        }
    }

    private static class DeleteProductAsyncTask extends AsyncTask<Product, Void, Void> {

        private ProductDao mAsyncTaskDao;

        DeleteProductAsyncTask(ProductDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Product... params) {
            mAsyncTaskDao.deleteProduct(params[0]);
            return null;
        }
    }

    public void getSimilarbyTypeMaterial(String type, String material, int id, GetSimilarProductsListener getSimilarProductsListener) {
        new GetSimilarbyTypeMaterialAsyncTask(mProductDao, getSimilarProductsListener, type, material, id).execute();
    }


    public class GetSimilarbyTypeMaterialAsyncTask extends AsyncTask<Void, Void, List<Product>> {

        private ProductDao mAsyncTaskDao;
        private GetSimilarProductsListener mListener;
        private String mType;
        private String mMaterial;
        private int mId;

        public GetSimilarbyTypeMaterialAsyncTask(ProductDao dao, GetSimilarProductsListener listener, String type, String material, int id) {
            mAsyncTaskDao = dao;
            mListener = listener;
            this.mType = type;
            this.mMaterial = material;
            this.mId = id;
        }

        @Override
        protected List<Product> doInBackground(Void... voids) {
            return mAsyncTaskDao.getSimilarbyTypeMaterial(mType, mMaterial, mId);
        }

        @Override
        synchronized protected void onPostExecute(List<Product> isAnyReceived) {
            super.onPostExecute(isAnyReceived);
            if (isAnyReceived != null) {
                mListener.onReceivedProducts(isAnyReceived);
            } else {
                mListener.onVoidProducts("No existen productos similares");
            }
        }
    }
}
