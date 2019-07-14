package org.kknickkk.spider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jcraft.jsch.Session;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.kknickkk.spider.Tasks.DownloadTask;
import org.kknickkk.spider.Tasks.GetFilesTask;
import org.kknickkk.spider.Tasks.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FolderActivity extends AppCompatActivity {

    ArrayList<DirectoryElement> elements = new ArrayList<DirectoryElement>();
    ProgressDialog mProgressDialog;
    FolderAdapter adapter;
    Uri uri;
    byte[] fileUpBytes;
    final PathHandler pathHandler = new PathHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("FOLDER ACTIVITY", "started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Globals.toolbar = toolbar;

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Upload a file", Snackbar.LENGTH_LONG).show();

                performFileSearchUpload();
            }
        });

        Globals.elements = elements;
        RecyclerView rvConnections = findViewById(R.id.rvConnections);
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_view);

        Session session = Globals.session;
        Log.d("FOLDER ACTIVITY", "got session: " + session.getHost());
        // set the adapter
        adapter = new FolderAdapter(elements);
        Globals.rvAdapter = adapter;

        //get file list in task
        GetFilesTask getFilesTask = new GetFilesTask();
        getFilesTask.execute(pathHandler.getCurrentPath());

        // link all

        rvConnections.setAdapter(adapter);
        rvConnections.setLayoutManager(new LinearLayoutManager(this));

        rvConnections.addOnItemTouchListener(
                new RecyclerItemClickListener(this,rvConnections ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        DirectoryElement element = elements.get(position);
                        Log.d("ELEMENT", "onClick " + element.name);
                        if(element.isDirectory || element.sftpInfo.getAttrs().isLink()){
                            pathHandler.updatePath(element.name);

                            new GetFilesTask().execute(pathHandler.getCurrentPath());

                            }
                        }


                    @Override public void onLongItemClick(View view, int position) {
                        DirectoryElement element = elements.get(position);
                        Log.d("ELEMENT", "onClick LONG " + position + " " + element.name);


                        if(!element.isDirectory && !element.sftpInfo.getAttrs().isLink()) {
                            Snackbar.make(view, "Download: " + element.name, Snackbar.LENGTH_LONG).show();


                            mProgressDialog = new ProgressDialog(FolderActivity.this);
                            mProgressDialog.setMessage("Downloading: " + element.getShortname() + " (" + element.getSizeMB() + "MB)");
                            mProgressDialog.setIndeterminate(true);
                            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            mProgressDialog.setCancelable(true);
                            Globals.mProgressDialogDownload = mProgressDialog;

                            final DownloadTask downloadTask = new DownloadTask(FolderActivity.this);
                            downloadTask.execute(element);


                            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    downloadTask.cancel(true); //cancel the task
                                }
                            });
                        }
                    }
                })
        );


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetFilesTask().execute(pathHandler.getCurrentPath());
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }


    @Override
    public void onBackPressed() {
        pathHandler.updatePath("..");
        new GetFilesTask().execute(pathHandler.getCurrentPath());

    }


    @Override
    public void onResume(){
        super.onResume();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Globals.channel.disconnect();
            Globals.session.disconnect();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    private static final int READ_REQUEST_CODE_UPLOAD = 84;

    /**
     * Fires an intent to spin up the "file chooser" UI and select an image.
     */
    public void performFileSearchUpload() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("*/*");

        startActivityForResult(intent, READ_REQUEST_CODE_UPLOAD);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE_UPLOAD && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            //Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Globals.fileUpName = getFileName(uri);

                Log.d("UPLOAD", "got: " + uri.toString());

                try {
                    fileUpBytes = readBytes(getContentResolver().openInputStream(uri));
                    Globals.fileUpBytes = fileUpBytes;

                    mProgressDialog = new ProgressDialog(FolderActivity.this);
                    mProgressDialog.setMessage("Uploading: " + uri.getLastPathSegment());
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setCancelable(true);
                    Globals.mProgressDialogUpload = mProgressDialog;

                    final UploadTask uploadTask = new UploadTask(FolderActivity.this);
                    uploadTask.execute(Globals.currentPath);


                    mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface dialog) {
                            uploadTask.cancel(true); //cancel the task
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }


    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public byte[] readBytes(InputStream inputStream) throws IOException {
        // this dynamically extends to take the bytes you read
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }



}
