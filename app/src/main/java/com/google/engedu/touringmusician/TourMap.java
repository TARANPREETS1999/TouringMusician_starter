/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.touringmusician;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView

public class TourMap extends View {

    private Bitmap mapImage;
    private arrayList nodelist = new arrayList();
    private CircularLinkedList begneerlist = new CircularLinkedList();
    private CircularLinkedList closestlist = new CircularLinkedList();
    private CircularLinkedList smallestlist = new CircularLinkedList();
    private String insertMode = "Add";

    public TourMap(Context context) {
        super(context);
        mapImage = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.map);
        modeslist.add(begneerlist);
        modeslist.add(smallestlist);
        modeslist.add(closestlist);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mapImage, 0, 0, null);
        Paint pointPaint = new Paint();

        for(CircularLinkedList list : modes) {
            int index = modes.indexOf(list);
            // change colors depending on mode
            if(index == 0){
                pointPaint.setColor(Color.BLUE);
            }
            if(index == 1){
                pointPaint.setColor(Color.BLACK);
            }
            if(index == 2){
                pointPaint.setColor(Color.RED);
            }
            pointPaint.setStyle(Paint.Style.FILL);

            // keep track of the previous node for drawing the lines
            Point beginning = null;
            Point finalp = null;
            Point previous = null;
            for (Point p : list) {
                if (previous != null) {
                    // draw the line if you have a previous point
                    canvas.drawLine(previous.x, previous.y, p.x, p.y, pointPaint);
                } else
                    beginning = p;
                canvas.drawCircle(p.x, p.y, 5, pointPaint);
                previous = p;
                finalp = p;
            }
            if (finalp != null && beginning != null)
                canvas.drawLine(finalp.x, finalp.y, beginning.x, beginning.y, pointPaint);
        }
    @Override
    public boolean onTouchEvent(MotionEvent event){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Point p = new Point((int) event.getX(), (int) event.getY());
                    if (insertMode.equals("Closest")) {
                        closestList.insertNearest(p);
                    } else if (insertMode.equals("Smallest")) {
                        smallestList.insertSmallest(p);
                    } else {
                        beginList.insertBeginning(p);
                    }
                    TextView message1 = (TextView) ((Activity) getContext()).findViewById(R.id.game_status1);
                    TextView message2 = (TextView) ((Activity) getContext()).findViewById(R.id.game_status2);
                    TextView message3 = (TextView) ((Activity) getContext()).findViewById(R.id.game_status3);
                    if (message1 != null) {
                        float totalDistance = beginList.totalDistance();
                        message1.setText(String.format("beginList Tour length is now %.2f", totalDistance));
                    }
                    if (message2 != null) {
                        float totalDistance = closestList.totalDistance();
                        message2.setText(String.format("closestList Tour length is now %.2f", totalDistance));
                    }
                    if (message3 != null) {
                        float totalDistance = smallestList.totalDistance();
                        message3.setText(String.format("smallestList Tour length is now %.2f", totalDistance));
                    }
                    invalidate();
                    return true;
            }
            return super.onTouchEvent(event);
        }


        public void reset() {
        beginList.reset();
        closestList.reset();
        smallestList.reset();
        invalidate();
    }

    public void setInsertMode(String mode) {
        insertMode = mode;
    }
}
