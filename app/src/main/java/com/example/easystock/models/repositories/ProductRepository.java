package com.example.easystock.models.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.easystock.models.Product;
import com.example.easystock.models.User;
import com.example.easystock.models.dao.ProductDao;
import com.example.easystock.models.dao.UserDao;
import com.example.easystock.models.db.AppDatabase;

import java.util.List;

public class ProductRepository {

    private ProductDao mProductDao;
    private LiveData<List<Product>> mAllProducts;

    public ProductRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mProductDao = db.mProductDao();
        mAllProducts = mProductDao.getAllProducts();
    }

    public LiveData<List<Product>> getAllProducts() {
        return mAllProducts;
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
}
