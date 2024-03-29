package com.jonglen7.jugglinglab.jugglinglab.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.jonglen7.jugglinglab.R;
import com.jonglen7.jugglinglab.util.ColorConverter;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * Class Floor
 * Build a 8x8 grid floor
 * 
 * @author frederic.rayar
 * @date 21/06/2012
 *
 */
public class Floor {
	
	
	/**
	 * Attributes
	 * 
	 */
    private final float FLOOR_POSITION = -3.0f; // For bouncing balls

    private FloatBuffer   mVertexBuffer;
    private ByteBuffer  mIndexBuffer;
    private ByteBuffer vbb;
	private float floorVertices[];
    private byte floorIndices[] = {
            0,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,31,
            0,23,1,24,2,25,3,26,4,27,5,28,6,29,7,30,8,31  
   	};

    private Context context = null;
    private SharedPreferences preferences = null;
    
    /**
     * Constructor
     * 
     */
    public Floor(Context context)
    {   
        this.context = context;
        this.preferences = context.getSharedPreferences("com.jonglen7.jugglinglab_preferences", 0);
        
        int size = 3*32;	// 3 coordinates x 32 border points for a 9x9 Grid
		floorVertices = new float[size];
    	
		vbb = ByteBuffer.allocateDirect(size*4); 			
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
    	mIndexBuffer = ByteBuffer.allocateDirect(floorIndices.length);
    	
    	BuildFloor();
    }
	

	
	// Build the floor that will be rendered using OpenGL.
    // 0  1  2  3  4  5  6  7  8
    // 9                       10
    // 11                      12
    // 13                      14
    // 15                      16
    // 17                      18
    // 19                      20
    // 21                      22
    // 23 24 25 26 27 28 29 30 31
    //
	private void BuildFloor()
	{
	    int a = 0;
	    float i = -50.0f;
	   
	    for (float j = -50.0f; j <= 50.0; j += 12.5f, a+=3) 
	    {
	        floorVertices[a] = j;
	        floorVertices[a+1] = FLOOR_POSITION;
	        floorVertices[a+2] = i;
	    }       

	    i+=12.5f;
	    for (float k = i; k<50.0; k+=12.5f, a+=6)
	    {
	        floorVertices[a] = -50.0f;
	        floorVertices[a+1] = FLOOR_POSITION;
	        floorVertices[a+2] = k;

	        floorVertices[a+3] = 50.0f;
	        floorVertices[a+4] = FLOOR_POSITION;
	        floorVertices[a+5] = k;
	    }

	    i=50.0f;
	    for (float j = -50.0f; j <= 50.0; j += 12.5f, a+=3) 
	    {
	        floorVertices[a] = j;
	        floorVertices[a+1] = FLOOR_POSITION;
	        floorVertices[a+2] = i;
	    }

	}

	
	// Draw the lines that make up the floor, using OpenGL
	public void draw(GL10 gl)
	{
    	mVertexBuffer.put(floorVertices);
        mVertexBuffer.position(0);

        int color = preferences.getInt("floor_color", context.getResources().getInteger(R.color.floor_default_color));
        float[] rgba = new ColorConverter().hex2rgba(color);
        
    	//gl.glDisable(GL10.GL_BLEND);
    	gl.glColor4f(rgba[0],rgba[1],rgba[2],rgba[3]);
    	gl.glLineWidth(1.0f);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
              
        mIndexBuffer.put(floorIndices);
        mIndexBuffer.position(0);
    	gl.glDrawElements(GL10.GL_LINES, floorIndices.length, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);

	}
	

}
