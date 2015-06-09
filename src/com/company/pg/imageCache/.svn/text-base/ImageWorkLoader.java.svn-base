/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.company.pg.imageCache;
import java.lang.ref.WeakReference;

import com.company.pg.ILog;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * This class wraps up completing some arbitrary long running work when loading a bitmap to an ImageView. It handles
 * things like using a memory and disk cache, running the work in a background thread and setting a placeholder image.
 */
public abstract class ImageWorkLoader {
	
	private static final String TAG = "ImageWorkLoader";
	private static final int FADE_IN_TIME = 200;
	protected Context mContext;
	private Bitmap mLoadingBitmap;
	private boolean mExitTasksEarly = false;
	
	protected ImageWorkLoader(Context context) {
		mContext = context;
	}
	
	/**
	 * Load an image specified by the data parameter into an ImageView (override
	 * {@link ImageWorker#processBitmap(Object)} to define the processing logic). A memory and disk cache will be used
	 * if an {@link ImageCache} has been set using {@link ImageWorker#setImageCache(ImageCache)}. If the image is found
	 * in the memory cache, it is set immediately, otherwise an {@link AsyncTask} will be created to asynchronously load
	 * the bitmap.
	 * 
	 * @param data
	 *            The URL of the image to download.
	 * @param imageView
	 *            The ImageView to bind the downloaded image to.
	 */
	public void loadImage(String data, ImageView imageView) {
		Bitmap bitmap = ImageCache.getInstance().getBitmapFromMemCache(data);
		// Bitmap found in memory cache
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else if (cancelPotentialWork(data, imageView)) {
			final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
			final AsyncDrawable asyncDrawable = new AsyncDrawable(mContext.getResources(), mLoadingBitmap, task);
			imageView.setImageDrawable(asyncDrawable);
			task.execute(data);
		}
	}
	
	/**
	 * Set placeholder bitmap that shows when the the background thread is running.
	 * 
	 * @param bitmap
	 */
	public void setLoadingImage(Bitmap bitmap) {
		mLoadingBitmap = bitmap;
	}
	
	/**
	 * Set placeholder bitmap that shows when the the background thread is running.
	 * 
	 * @param resId
	 */
	public void setLoadingImage(int resId) {
		mLoadingBitmap = BitmapFactory.decodeResource(mContext.getResources(), resId);
	}
	
	public void setExitTasksEarly(boolean exitTasksEarly) {
		mExitTasksEarly = exitTasksEarly;
	}
	
	/**
	 * Subclasses should override this to define any processing or work that must happen to produce the final bitmap.
	 * This will be executed in a background thread and be long running. For example, you could resize a large bitmap
	 * here, or pull down an image from the network.
	 * 
	 * @param data
	 *            The data to identify which image to process, as provided by
	 *            {@link ImageWorkLoader#loadImage(Object, ImageView)}
	 * @return The processed bitmap
	 */
	protected abstract Bitmap processBitmap(Object data);
	
	/**
	 * Returns true if the current work has been canceled or if there was no work in progress on this image view.
	 * Returns false if the work in progress deals with the same data. The work is not stopped in that case.
	 */
	public static boolean cancelPotentialWork(String data, ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
		
		if (bitmapWorkerTask != null) {
			final String bitmapData = bitmapWorkerTask.url;
			if (bitmapData == null || !bitmapData.equals(data)) {
				bitmapWorkerTask.cancel(true);
				ILog.LogD(ImageWorkLoader.class, "cancelPotentialWork - cancelled work for " + data);
			} else {
				// The same work is already in progress.
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @param imageView
	 *            Any imageView
	 * @return Retrieve the currently active work task (if any) associated with this imageView. null if there is no such
	 *         task.
	 */
	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}
	
	/**
	 * The actual AsyncTask that will asynchronously process the image.
	 */
	private class BitmapWorkerTask extends ImageAsyncTask<String, Void, Bitmap> {
		
		private String url;
		private final WeakReference<ImageView> imageViewReference;
		
		public BitmapWorkerTask(ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);
		}
		
		/**
		 * Background processing.
		 */
		@Override
		protected Bitmap doInBackground(String... params) {
			url = params[0];
			Bitmap bitmap = null;
			
			// Log.i(TAG, "doInBackground" + params + " isCancelled # " + isCancelled() + "#getAttachedImageView " +
			// getAttachedImageView());
			
			// If this task has not been cancelled by another thread and
			// the ImageView that was originally bound to this task is still
			// bound back to this task and our "exit early" flag is not set, then call the main
			// process method (as implemented by a subclass)
			if (!isCancelled() && getAttachedImageView() != null && !mExitTasksEarly) {
				bitmap = processBitmap(params[0]);
			}
			
			// Log.i(TAG, "doInBackground" + bitmap + " @@" + params + " isCancelled # " + isCancelled() +
			// "#getAttachedImageView " + getAttachedImageView());
			return bitmap;
		}
		
		/**
		 * Once the image is processed, associates it to the imageView
		 */
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			// if cancel was called on this task or the "exit early" flag is set then we're done
			if (isCancelled() || mExitTasksEarly) {
				if (bitmap != null) {
					bitmap.recycle();
					bitmap = null;
				}
				return;
			}
			// If the bitmap was processed and the image cache is available, then add the processed
			// bitmap to the cache for future use. Note we don't check if the task was cancelled
			// here, if it was, and the thread is still running, we may as well add the processed
			// bitmap to our cache as it might be used again in the future
			if (bitmap != null) {
				ImageCache.getInstance().addBitmapToCache(url, bitmap);
			}
			
			final ImageView imageView = getAttachedImageView();
			
			// Log.e(TAG, "doInBackground isCancelled # " + isCancelled() + "# " + bitmap + "@@" + imageView);
			if (bitmap != null && imageView != null) {
				imageView.setImageBitmap(bitmap);
			}
		}
		
		/**
		 * Returns the ImageView associated with this task as long as the ImageView's task still points to this task as
		 * well. Returns null otherwise.
		 */
		private ImageView getAttachedImageView() {
			final ImageView imageView = imageViewReference.get();
			final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
			
			if (this == bitmapWorkerTask) {
				return imageView;
			}
			
			return null;
		}
	}
	
	/**
	 * A custom Drawable that will be attached to the imageView while the work is in progress. Contains a reference to
	 * the actual worker task, so that it can be stopped if a new binding is required, and makes sure that only the last
	 * started worker process can bind its result, independently of the finish order.
	 */
	private static class AsyncDrawable extends BitmapDrawable {
		
		private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;
		
		public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
			super(res, bitmap);
			
			bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
		}
		
		public BitmapWorkerTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}
}
