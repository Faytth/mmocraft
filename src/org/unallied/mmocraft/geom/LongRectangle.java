package org.unallied.mmocraft.geom;

import java.beans.Transient;

/**
 * Implements a long version of Java's Rectangle.
 * @author Alexandria
 *
 */
public class LongRectangle {

    /**
     * The X coordinate of the upper-left corner of the <code>LongRectangle</code>.
     *
     * @serial
     * @see #setLocation(int, int)
     * @see #getLocation()
     * @since 1.0
     */
    public long x;

    /**
     * The Y coordinate of the upper-left corner of the <code>LongRectangle</code>.
     *
     * @serial
     * @see #setLocation(int, int)
     * @see #getLocation()
     * @since 1.0
     */
    public long y;

    /**
     * The width of the <code>LongRectangle</code>.
     * @serial
     * @see #setSize(int, int)
     * @see #getSize()
     * @since 1.0
     */
    public long width;

    /**
     * The height of the <code>LongRectangle</code>.
     *
     * @serial
     * @see #setSize(int, int)
     * @see #getSize()
     * @since 1.0
     */
    public long height;

    /*
     * JDK 1.1 serialVersionUID
     */
     private static final long serialVersionUID = -4345857070255674764L;

    /**
     * Initialize JNI field and method IDs
     */
    private static native void initIDs();

    /**
     * Constructs a new <code>LongRectangle</code> whose upper-left corner
     * is at (0,&nbsp;0) in the coordinate space, and whose width and
     * height are both zero.
     */
    public LongRectangle() {
        this(0, 0, 0, 0);
    }

    /**
     * Constructs a new <code>LongRectangle</code>, initialized to match
     * the values of the specified <code>LongRectangle</code>.
     * @param r  the <code>LongRectangle</code> from which to copy initial values
     *           to a newly constructed <code>LongRectangle</code>
     * @since 1.1
     */
    public LongRectangle(LongRectangle r) {
        this(r.x, r.y, r.width, r.height);
    }

    /**
     * Constructs a new <code>LongRectangle</code> whose upper-left corner is
     * specified as
     * {@code (x,y)} and whose width and height
     * are specified by the arguments of the same name.
     * @param     x the specified X coordinate
     * @param     y the specified Y coordinate
     * @param     width    the width of the <code>LongRectangle</code>
     * @param     height   the height of the <code>LongRectangle</code>
     * @since 1.0
     */
    public LongRectangle(long x, long y, long width, long height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Constructs a new <code>LongRectangle</code> whose upper-left corner
     * is at (0,&nbsp;0) in the coordinate space, and whose width and
     * height are specified by the arguments of the same name.
     * @param width the width of the <code>LongRectangle</code>
     * @param height the height of the <code>LongRectangle</code>
     */
    public LongRectangle(long width, long height) {
        this(0, 0, width, height);
    }

    /**
     * Returns the X coordinate of the bounding <code>LongRectangle</code> in
     * <code>long</code> precision.
     * @return the X coordinate of the bounding <code>LongRectangle</code>.
     */
    public long getX() {
        return x;
    }

    /**
     * Returns the Y coordinate of the bounding <code>LongRectangle</code> in
     * <code>double</code> precision.
     * @return the Y coordinate of the bounding <code>LongRectangle</code>.
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the width of the bounding <code>LongRectangle</code> in
     * <code>double</code> precision.
     * @return the width of the bounding <code>LongRectangle</code>.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Returns the height of the bounding <code>LongRectangle</code> in
     * <code>double</code> precision.
     * @return the height of the bounding <code>LongRectangle</code>.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Gets the bounding <code>LongRectangle</code> of this <code>LongRectangle</code>.
     * <p>
     * This method is included for completeness, to parallel the
     * <code>getBounds</code> method of
     * {@link Component}.
     * @return    a new <code>LongRectangle</code>, equal to the
     * bounding <code>LongRectangle</code> for this <code>LongRectangle</code>.
     * @see       java.awt.Component#getBounds
     * @see       #setBounds(LongRectangle)
     * @see       #setBounds(int, int, int, int)
     * @since     1.1
     */
    @Transient
    public LongRectangle getBounds() {
        return new LongRectangle(x, y, width, height);
    }

    /**
     * {@inheritDoc}
     * @since 1.2
     */
    public LongRectangle getBounds2D() {
        return new LongRectangle(x, y, width, height);
    }

    /**
     * Sets the bounding <code>LongRectangle</code> of this <code>LongRectangle</code>
     * to match the specified <code>LongRectangle</code>.
     * <p>
     * This method is included for completeness, to parallel the
     * <code>setBounds</code> method of <code>Component</code>.
     * @param r the specified <code>LongRectangle</code>
     * @see       #getBounds
     * @see       java.awt.Component#setBounds(java.awt.Rectangle)
     * @since     1.1
     */
    public void setBounds(LongRectangle r) {
        setBounds(r.x, r.y, r.width, r.height);
    }

    /**
     * Sets the bounding <code>LongRectangle</code> of this
     * <code>LongRectangle</code> to the specified
     * <code>x</code>, <code>y</code>, <code>width</code>,
     * and <code>height</code>.
     * <p>
     * This method is included for completeness, to parallel the
     * <code>setBounds</code> method of <code>Component</code>.
     * @param x the new X coordinate for the upper-left
     *                    corner of this <code>LongRectangle</code>
     * @param y the new Y coordinate for the upper-left
     *                    corner of this <code>LongRectangle</code>
     * @param width the new width for this <code>LongRectangle</code>
     * @param height the new height for this <code>LongRectangle</code>
     * @see       #getBounds
     * @see       java.awt.Component#setBounds(int, int, int, int)
     * @since     1.1
     */
    public void setBounds(long x, long y, long width, long height) {
        reshape(x, y, width, height);
    }


    /**
     * Sets the bounding <code>LongRectangle</code> of this
     * <code>LongRectangle</code> to the specified
     * <code>x</code>, <code>y</code>, <code>width</code>,
     * and <code>height</code>.
     * <p>
     * @param x the new X coordinate for the upper-left
     *                    corner of this <code>LongRectangle</code>
     * @param y the new Y coordinate for the upper-left
     *                    corner of this <code>LongRectangle</code>
     * @param width the new width for this <code>LongRectangle</code>
     * @param height the new height for this <code>LongRectangle</code>
     * @deprecated As of JDK version 1.1,
     * replaced by <code>setBounds(int, int, int, int)</code>.
     */
    @Deprecated
    public void reshape(long x, long y, long width, long height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Moves this <code>LongRectangle</code> to the specified location.
     * <p>
     * This method is included for completeness, to parallel the
     * <code>setLocation</code> method of <code>Component</code>.
     * @param x the X coordinate of the new location
     * @param y the Y coordinate of the new location
     * @see       #getLocation
     * @see       java.awt.Component#setLocation(int, int)
     * @since     1.1
     */
    public void setLocation(long x, long y) {
        move(x, y);
    }

    /**
     * Moves this <code>LongRectangle</code> to the specified location.
     * <p>
     * @param x the X coordinate of the new location
     * @param y the Y coordinate of the new location
     * @deprecated As of JDK version 1.1,
     * replaced by <code>setLocation(int, int)</code>.
     */
    @Deprecated
    public void move(long x, long y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Translates this <code>LongRectangle</code> the indicated distance,
     * to the right along the X coordinate axis, and
     * downward along the Y coordinate axis.
     * @param dx the distance to move this <code>LongRectangle</code>
     *                 along the X axis
     * @param dy the distance to move this <code>LongRectangle</code>
     *                 along the Y axis
     * @see       java.awt.Rectangle#setLocation(int, int)
     * @see       java.awt.Rectangle#setLocation(java.awt.Point)
     */
    public void translate(long dx, long dy) {
        long oldv = this.x;
        long newv = oldv + dx;
        if (dx < 0) {
            // moving leftward
            if (newv > oldv) {
                // negative overflow
                // Only adjust width if it was valid (>= 0).
                if (width >= 0) {
                    // The right edge is now conceptually at
                    // newv+width, but we may move newv to prevent
                    // overflow.  But we want the right edge to
                    // remain at its new location in spite of the
                    // clipping.  Think of the following adjustment
                    // conceptually the same as:
                    // width += newv; newv = MIN_VALUE; width -= newv;
                    width += newv - Long.MIN_VALUE;
                    // width may go negative if the right edge went past
                    // MIN_VALUE, but it cannot overflow since it cannot
                    // have moved more than MIN_VALUE and any non-negative
                    // number + MIN_VALUE does not overflow.
                }
                newv = Long.MIN_VALUE;
            }
        } else {
            // moving rightward (or staying still)
            if (newv < oldv) {
                // positive overflow
                if (width >= 0) {
                    // Conceptually the same as:
                    // width += newv; newv = MAX_VALUE; width -= newv;
                    width += newv - Long.MAX_VALUE;
                    // With large widths and large displacements
                    // we may overflow so we need to check it.
                    if (width < 0) width = Long.MAX_VALUE;
                }
                newv = Long.MAX_VALUE;
            }
        }
        this.x = newv;

        oldv = this.y;
        newv = oldv + dy;
        if (dy < 0) {
            // moving upward
            if (newv > oldv) {
                // negative overflow
                if (height >= 0) {
                    height += newv - Long.MIN_VALUE;
                    // See above comment about no overflow in this case
                }
                newv = Long.MIN_VALUE;
            }
        } else {
            // moving downward (or staying still)
            if (newv < oldv) {
                // positive overflow
                if (height >= 0) {
                    height += newv - Long.MAX_VALUE;
                    if (height < 0) height = Long.MAX_VALUE;
                }
                newv = Long.MAX_VALUE;
            }
        }
        this.y = newv;
    }

    /**
     * Sets the size of this <code>LongRectangle</code> to the specified
     * width and height.
     * <p>
     * This method is included for completeness, to parallel the
     * <code>setSize</code> method of <code>Component</code>.
     * @param width the new width for this <code>LongRectangle</code>
     * @param height the new height for this <code>LongRectangle</code>
     * @see       java.awt.Component#setSize(int, int)
     * @see       #getSize
     * @since     1.1
     */
    public void setSize(long width, long height) {
        resize(width, height);
    }

    /**
     * Sets the size of this <code>LongRectangle</code> to the specified
     * width and height.
     * <p>
     * @param width the new width for this <code>LongRectangle</code>
     * @param height the new height for this <code>LongRectangle</code>
     * @deprecated As of JDK version 1.1,
     * replaced by <code>setSize(int, int)</code>.
     */
    @Deprecated
    public void resize(long width, long height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Checks whether or not this <code>LongRectangle</code> contains the
     * point at the specified location {@code (x,y)}.
     *
     * @param  x the specified X coordinate
     * @param  y the specified Y coordinate
     * @return    <code>true</code> if the point
     *            {@code (x,y)} is inside this
     *            <code>LongRectangle</code>;
     *            <code>false</code> otherwise.
     * @since     1.1
     */
    public boolean contains(long x, long y) {
        return inside(x, y);
    }

    /**
     * Checks whether or not this <code>LongRectangle</code> entirely contains
     * the specified <code>LongRectangle</code>.
     *
     * @param     r   the specified <code>LongRectangle</code>
     * @return    <code>true</code> if the <code>LongRectangle</code>
     *            is contained entirely inside this <code>LongRectangle</code>;
     *            <code>false</code> otherwise
     * @since     1.2
     */
    public boolean contains(LongRectangle r) {
        return contains(r.x, r.y, r.width, r.height);
    }

    /**
     * Checks whether this <code>LongRectangle</code> entirely contains
     * the <code>LongRectangle</code>
     * at the specified location {@code (X,Y)} with the
     * specified dimensions {@code (W,H)}.
     * @param     X the specified X coordinate
     * @param     Y the specified Y coordinate
     * @param     W   the width of the <code>LongRectangle</code>
     * @param     H   the height of the <code>LongRectangle</code>
     * @return    <code>true</code> if the <code>LongRectangle</code> specified by
     *            {@code (X, Y, W, H)}
     *            is entirely enclosed inside this <code>LongRectangle</code>;
     *            <code>false</code> otherwise.
     * @since     1.1
     */
    public boolean contains(long X, long Y, long W, long H) {
        long w = this.width;
        long h = this.height;
        if ((w | h | W | H) < 0) {
            // At least one of the dimensions is negative...
            return false;
        }
        // Note: if any dimension is zero, tests below must return false...
        long x = this.x;
        long y = this.y;
        if (X < x || Y < y) {
            return false;
        }
        w += x;
        W += X;
        if (W <= X) {
            // X+W overflowed or W was zero, return false if...
            // either original w or W was zero or
            // x+w did not overflow or
            // the overflowed x+w is smaller than the overflowed X+W
            if (w >= x || W > w) return false;
        } else {
            // X+W did not overflow and W was not zero, return false if...
            // original w was zero or
            // x+w did not overflow and x+w is smaller than X+W
            if (w >= x && W > w) return false;
        }
        h += y;
        H += Y;
        if (H <= Y) {
            if (h >= y || H > h) return false;
        } else {
            if (h >= y && H > h) return false;
        }
        return true;
    }

    /**
     * Checks whether or not this <code>LongRectangle</code> contains the
     * point at the specified location {@code (X,Y)}.
     *
     * @param  X the specified X coordinate
     * @param  Y the specified Y coordinate
     * @return    <code>true</code> if the point
     *            {@code (X,Y)} is inside this
     *            <code>LongRectangle</code>;
     *            <code>false</code> otherwise.
     * @deprecated As of JDK version 1.1,
     * replaced by <code>contains(int, int)</code>.
     */
    @Deprecated
    public boolean inside(long X, long Y) {
        long w = this.width;
        long h = this.height;
        if ((w | h) < 0) {
            // At least one of the dimensions is negative...
            return false;
        }
        // Note: if either dimension is zero, tests below must return false...
        long x = this.x;
        long y = this.y;
        if (X < x || Y < y) {
            return false;
        }
        w += x;
        h += y;
        //    overflow || intersect
        return ((w < x || w > X) &&
                (h < y || h > Y));
    }

    /**
     * Determines whether or not this <code>LongRectangle</code> and the specified
     * <code>LongRectangle</code> intersect. Two rectangles intersect if
     * their intersection is nonempty.
     *
     * @param r the specified <code>LongRectangle</code>
     * @return    <code>true</code> if the specified <code>LongRectangle</code>
     *            and this <code>LongRectangle</code> intersect;
     *            <code>false</code> otherwise.
     */
    public boolean intersects(LongRectangle r) {
        long tw = this.width;
        long th = this.height;
        long rw = r.width;
        long rh = r.height;
        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
            return false;
        }
        long tx = this.x;
        long ty = this.y;
        long rx = r.x;
        long ry = r.y;
        rw += rx;
        rh += ry;
        tw += tx;
        th += ty;
        //      overflow || intersect
        return ((rw < rx || rw > tx) &&
                (rh < ry || rh > ty) &&
                (tw < tx || tw > rx) &&
                (th < ty || th > ry));
    }

    /**
     * Computes the intersection of this <code>LongRectangle</code> with the
     * specified <code>LongRectangle</code>. Returns a new <code>LongRectangle</code>
     * that represents the intersection of the two rectangles.
     * If the two rectangles do not intersect, the result will be
     * an empty rectangle.
     *
     * @param     r   the specified <code>LongRectangle</code>
     * @return    the largest <code>LongRectangle</code> contained in both the
     *            specified <code>LongRectangle</code> and in
     *            this <code>LongRectangle</code>; or if the rectangles
     *            do not intersect, an empty rectangle.
     */
    public LongRectangle intersection(LongRectangle r) {
        long tx1 = this.x;
        long ty1 = this.y;
        long rx1 = r.x;
        long ry1 = r.y;
        // Note:  These could mess up if the width / height is super crazy long and near max x / y.
        long tx2 = tx1; tx2 += this.width;
        long ty2 = ty1; ty2 += this.height;
        long rx2 = rx1; rx2 += r.width;
        long ry2 = ry1; ry2 += r.height;
        if (tx1 < rx1) tx1 = rx1;
        if (ty1 < ry1) ty1 = ry1;
        if (tx2 > rx2) tx2 = rx2;
        if (ty2 > ry2) ty2 = ry2;
        tx2 -= tx1;
        ty2 -= ty1;
        // tx2,ty2 will never overflow (they will never be
        // larger than the smallest of the two source w,h)
        // they might underflow, though...
        if (tx2 < Integer.MIN_VALUE) tx2 = Integer.MIN_VALUE;
        if (ty2 < Integer.MIN_VALUE) ty2 = Integer.MIN_VALUE;
        return new LongRectangle(tx1, ty1, tx2, ty2);
    }

    /**
     * Computes the union of this <code>LongRectangle</code> with the
     * specified <code>LongRectangle</code>. Returns a new
     * <code>LongRectangle</code> that
     * represents the union of the two rectangles.
     * <p>
     * If either {@code Rectangle} has any dimension less than zero
     * the rules for <a href=#NonExistant>non-existant</a> rectangles
     * apply.
     * If only one has a dimension less than zero, then the result
     * will be a copy of the other {@code Rectangle}.
     * If both have dimension less than zero, then the result will
     * have at least one dimension less than zero.
     * <p>
     * If the resulting {@code Rectangle} would have a dimension
     * too large to be expressed as an {@code int}, the result
     * will have a dimension of {@code Integer.MAX_VALUE} along
     * that dimension.
     * @param r the specified <code>LongRectangle</code>
     * @return    the smallest <code>LongRectangle</code> containing both
     *            the specified <code>LongRectangle</code> and this
     *            <code>LongRectangle</code>.
     */
    public LongRectangle union(LongRectangle r) {
        long tx2 = this.width;
        long ty2 = this.height;
        if ((tx2 | ty2) < 0) {
            // This rectangle has negative dimensions...
            // If r has non-negative dimensions then it is the answer.
            // If r is non-existant (has a negative dimension), then both
            // are non-existant and we can return any non-existant rectangle
            // as an answer.  Thus, returning r meets that criterion.
            // Either way, r is our answer.
            return new LongRectangle(r);
        }
        long rx2 = r.width;
        long ry2 = r.height;
        if ((rx2 | ry2) < 0) {
            return new LongRectangle(this);
        }
        long tx1 = this.x;
        long ty1 = this.y;
        tx2 += tx1;
        ty2 += ty1;
        long rx1 = r.x;
        long ry1 = r.y;
        rx2 += rx1;
        ry2 += ry1;
        if (tx1 > rx1) tx1 = rx1;
        if (ty1 > ry1) ty1 = ry1;
        if (tx2 < rx2) tx2 = rx2;
        if (ty2 < ry2) ty2 = ry2;
        tx2 -= tx1;
        ty2 -= ty1;
        // tx2,ty2 will never underflow since both original rectangles
        // were already proven to be non-empty
        // they might overflow, though...
        if (tx2 > Long.MAX_VALUE) tx2 = Long.MAX_VALUE;
        if (ty2 > Long.MAX_VALUE) ty2 = Long.MAX_VALUE;
        return new LongRectangle(tx1, ty1, tx2, ty2);
    }

    /**
     * Adds a point, specified by the integer arguments {@code newx,newy}
     * to the bounds of this {@code Rectangle}.
     * <p>
     * If this {@code Rectangle} has any dimension less than zero,
     * the rules for <a href=#NonExistant>non-existant</a>
     * rectangles apply.
     * In that case, the new bounds of this {@code Rectangle} will
     * have a location equal to the specified coordinates and
     * width and height equal to zero.
     * <p>
     * After adding a point, a call to <code>contains</code> with the
     * added point as an argument does not necessarily return
     * <code>true</code>. The <code>contains</code> method does not
     * return <code>true</code> for points on the right or bottom
     * edges of a <code>LongRectangle</code>. Therefore, if the added point
     * falls on the right or bottom edge of the enlarged
     * <code>LongRectangle</code>, <code>contains</code> returns
     * <code>false</code> for that point.
     * If the specified point must be contained within the new
     * {@code Rectangle}, a 1x1 rectangle should be added instead:
     * <pre>
     *     r.add(newx, newy, 1, 1);
     * </pre>
     * @param newx the X coordinate of the new point
     * @param newy the Y coordinate of the new point
     */
    public void add(long newx, long newy) {
        if ((width | height) < 0) {
            this.x = newx;
            this.y = newy;
            this.width = this.height = 0;
            return;
        }
        long x1 = this.x;
        long y1 = this.y;
        long x2 = this.width;
        long y2 = this.height;
        x2 += x1;
        y2 += y1;
        if (x1 > newx) x1 = newx;
        if (y1 > newy) y1 = newy;
        if (x2 < newx) x2 = newx;
        if (y2 < newy) y2 = newy;
        x2 -= x1;
        y2 -= y1;
        if (x2 > Long.MAX_VALUE) x2 = Long.MAX_VALUE;
        if (y2 > Long.MAX_VALUE) y2 = Long.MAX_VALUE;
        reshape(x1, y1, x2, y2);
    }

    /**
     * Adds a <code>LongRectangle</code> to this <code>LongRectangle</code>.
     * The resulting <code>LongRectangle</code> is the union of the two
     * rectangles.
     * <p>
     * If either {@code Rectangle} has any dimension less than 0, the
     * result will have the dimensions of the other {@code Rectangle}.
     * If both {@code Rectangle}s have at least one dimension less
     * than 0, the result will have at least one dimension less than 0.
     * <p>
     * If either {@code Rectangle} has one or both dimensions equal
     * to 0, the result along those axes with 0 dimensions will be
     * equivalent to the results obtained by adding the corresponding
     * origin coordinate to the result rectangle along that axis,
     * similar to the operation of the {@link #add(Point)} method,
     * but contribute no further dimension beyond that.
     * <p>
     * If the resulting {@code Rectangle} would have a dimension
     * too large to be expressed as an {@code int}, the result
     * will have a dimension of {@code Integer.MAX_VALUE} along
     * that dimension.
     * @param  r the specified <code>LongRectangle</code>
     */
    public void add(LongRectangle r) {
        long tx2 = this.width;
        long ty2 = this.height;
        if ((tx2 | ty2) < 0) {
            reshape(r.x, r.y, r.width, r.height);
        }
        long rx2 = r.width;
        long ry2 = r.height;
        if ((rx2 | ry2) < 0) {
            return;
        }
        long tx1 = this.x;
        long ty1 = this.y;
        tx2 += tx1;
        ty2 += ty1;
        long rx1 = r.x;
        long ry1 = r.y;
        rx2 += rx1;
        ry2 += ry1;
        if (tx1 > rx1) tx1 = rx1;
        if (ty1 > ry1) ty1 = ry1;
        if (tx2 < rx2) tx2 = rx2;
        if (ty2 < ry2) ty2 = ry2;
        tx2 -= tx1;
        ty2 -= ty1;
        // tx2,ty2 will never underflow since both original
        // rectangles were non-empty
        // they might overflow, though...
        if (tx2 > Integer.MAX_VALUE) tx2 = Integer.MAX_VALUE;
        if (ty2 > Integer.MAX_VALUE) ty2 = Integer.MAX_VALUE;
        reshape(tx1, ty1, tx2, ty2);
    }

    /**
     * Resizes the <code>LongRectangle</code> both horizontally and vertically.
     * <p>
     * This method modifies the <code>LongRectangle</code> so that it is
     * <code>h</code> units larger on both the left and right side,
     * and <code>v</code> units larger at both the top and bottom.
     * <p>
     * The new <code>LongRectangle</code> has {@code (x - h, y - v)}
     * as its upper-left corner,
     * width of {@code (width + 2h)},
     * and a height of {@code (height + 2v)}.
     * <p>
     * If negative values are supplied for <code>h</code> and
     * <code>v</code>, the size of the <code>LongRectangle</code>
     * decreases accordingly.
     * The {@code grow} method will check for integer overflow
     * and underflow, but does not check whether the resulting
     * values of {@code width} and {@code height} grow
     * from negative to non-negative or shrink from non-negative
     * to negative.
     * @param h the horizontal expansion
     * @param v the vertical expansion
     */
    public void grow(long h, long v) {
        long x0 = this.x;
        long y0 = this.y;
        long x1 = this.width;
        long y1 = this.height;
        x1 += x0;
        y1 += y0;

        x0 -= h;
        y0 -= v;
        x1 += h;
        y1 += v;

        if (x1 < x0) {
            // Non-existant in X direction
            // Final width must remain negative so subtract x0 before
            // it is clipped so that we avoid the risk that the clipping
            // of x0 will reverse the ordering of x0 and x1.
            x1 -= x0;
            if (x1 < Long.MIN_VALUE) x1 = Long.MIN_VALUE;
            if (x0 < Long.MIN_VALUE) x0 = Long.MIN_VALUE;
            else if (x0 > Long.MAX_VALUE) x0 = Long.MAX_VALUE;
        } else { // (x1 >= x0)
            // Clip x0 before we subtract it from x1 in case the clipping
            // affects the representable area of the rectangle.
            if (x0 < Long.MIN_VALUE) x0 = Long.MIN_VALUE;
            else if (x0 > Long.MAX_VALUE) x0 = Long.MAX_VALUE;
            x1 -= x0;
            // The only way x1 can be negative now is if we clipped
            // x0 against MIN and x1 is less than MIN - in which case
            // we want to leave the width negative since the result
            // did not intersect the representable area.
            if (x1 < Long.MIN_VALUE) x1 = Long.MIN_VALUE;
            else if (x1 > Long.MAX_VALUE) x1 = Long.MAX_VALUE;
        }

        if (y1 < y0) {
            // Non-existant in Y direction
            y1 -= y0;
            if (y1 < Long.MIN_VALUE) y1 = Long.MIN_VALUE;
            if (y0 < Long.MIN_VALUE) y0 = Long.MIN_VALUE;
            else if (y0 > Long.MAX_VALUE) y0 = Long.MAX_VALUE;
        } else { // (y1 >= y0)
            if (y0 < Long.MIN_VALUE) y0 = Long.MIN_VALUE;
            else if (y0 > Long.MAX_VALUE) y0 = Long.MAX_VALUE;
            y1 -= y0;
            if (y1 < Long.MIN_VALUE) y1 = Long.MIN_VALUE;
            else if (y1 > Long.MAX_VALUE) y1 = Long.MAX_VALUE;
        }

        reshape(x0, y0, x1, y1);
    }

    /**
     * {@inheritDoc}
     * @since 1.2
     */
    public boolean isEmpty() {
        return (width <= 0) || (height <= 0);
    }

    /**
     * Checks whether two rectangles are equal.
     * <p>
     * The result is <code>true</code> if and only if the argument is not
     * <code>null</code> and is a <code>LongRectangle</code> object that has the
     * same upper-left corner, width, and height as
     * this <code>LongRectangle</code>.
     * @param obj the <code>Object</code> to compare with
     *                this <code>LongRectangle</code>
     * @return    <code>true</code> if the objects are equal;
     *            <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof LongRectangle) {
            LongRectangle r = (LongRectangle)obj;
            return ((x == r.x) &&
                    (y == r.y) &&
                    (width == r.width) &&
                    (height == r.height));
        }
        return super.equals(obj);
    }

    /**
     * Returns a <code>String</code> representing this
     * <code>LongRectangle</code> and its values.
     * @return a <code>String</code> representing this
     *               <code>LongRectangle</code> object's coordinate and size values.
     */
    public String toString() {
        return getClass().getName() + "[x=" + x + ",y=" + y + ",width=" + width + ",height=" + height + "]";
    }
}
