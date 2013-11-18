//**********************************************************************
// Package
//*********************************************************************

package tests.gui.table;


//**********************************************************************
// Import list
//**********************************************************************

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This layout was inspired by the HTML table: it lays out components
 * much like the HTML table lays out table data. It is as capable as
 * the GridBagLayout, but much easier to use.
 * <h3>Attributes</h3>
 * <p>
 * When you create a TableLayout, you pass in a String which defines a
 * set of attributes for the table. The TableLayout is then assigned
 * to a Container. As you add each Component to the Container, you can
 * also associate the Component with its own set of attributes.
 * <p>
 * The format of the attributes is similar to HTML attributes. The
 * attributes are case insensitive and can be separated with
 * any whitespace. Attributes which take a value are followed by an
 * '=' and then an integer. Here's an example: "cols=6 rgap=2 cgap=5
 * w".
 * <p>
 * Attributes are evaluated from left to right. If you duplicate an
 * attribute, the right-most one wins.
 * <p>All attributes can be specified for both the table and the
 * individual Components. Some attributes are only used by the table,
 * some are only used by the Components and some are used by both: the
 * table instance is the default which each Component can override.
 * <h3>Rows and Columns</h3>
 * <p>
 * When you create a table, you will almost always specify the number
 * of columns in it. The default is 1. Columns are filled from left to
 * right. When all columns are filled, a new row is automatically
 * created.
 * <p>
 * You can override the default Component placement somewhat with "col"
 * and "skip". col takes as a value, the column number in which the
 * Component should be placed. Column numbers begin with 0. The
 * default is to place the Component in the next available table cell.
 * One caveat when using col is that if you have already passed the
 * given column, the layout adds another row and places the Component
 * in the column on that new row.
 * <p>
 * "skip" allows you to skip a number of cells. The default is 0. If
 * the layout reaches the end of the row, skipping continues on the
 * next row.
 * <p>
 * You can make a Component span multiple rows or columns with rspan
 * and cspan. The default value for these is 1. Later components will
 * skip over any occupied cells, which is particularly important to
 * note for row spanning.
 * <h3>Spacing</h3>
 * The space the TableLayout works with is inside the Container's
 * insets. Some Containers have insets of 0 (e.g. JPanel) and most
 * don't allow you to alter the insets. Since you will often want to
 * control the space between the table and the edges of the Container,
 * an "extra" table inset can be given: titop, tibottom, tileft and
 * tiright. Each of these takes a value, the pixel offset from the top,
 * bottom, etc. The default value of each is 0.
 * <p>
 * You can create some space between cells in the table. This space is
 * <em>only</em> placed between cells; never along the edges of the
 * table. This allows you to nest table layouts and keep consistent
 * cell spacing. The attributes used are rgap and cgap and their
 * default value is 0.
 * <p>
 * Within a cell, you can also create some space between the cells
 * edges and the Component in the cell. The attributes are itop,
 * ibottom, ileft and iright. Their default value is 0.
 * <h3>Placement and Filling</h3>
 *<p>
 * Given that you have something to draw and a space to draw it in,
 * you have some choices as to where to place it and how to fill it,
 * particularly when the drawing area is bigger than required.
 * <p>
 * Placement attributes allow you to place the item in one of eight
 * compass directions or centered. The entire table can be placed
 * within the container using tn, tne, te, tse, ts, tsw, tw, tnw and
 * tc. Components can be placed within their cell using n, ne, e, se,
 * s, sw, w, nw and c.
 * <p>
 * Fill attributes allow you to fill the item to cover all available
 * space. Horizontal and vertical filling are handled separately. For
 * tables, the attributes are tfh, tfv and tf. "tf" fills in both
 * directions. For Components, use fh, fv and f.
 * <p>
 * Placement attributes turn off all filling. Fill attributes turn off
 * placement, but only in the fill direction. So "n fh" will stretch a
 * Component horizontally, but will place it at the "north" position
 * (at the top of the cell).
 * <p>
 * The default value for both the table and the individual Components
 * is to fill in both directions. You will almost always want to
 * specify your own values.
 * <h3>Weighting</h3>
 *  <p>
 * When a table is filled, if the available space exceeds the space
 * required, we stretch the table to fill the space. This implies that
 * we have to stretch each cell. How much each cell should be filled
 * is what weighting is all about. The attributes are rweight and
 * cweight which take an integer weight factor. The default is 0.
 * <p>
 * Note that stretching a cell is not the same as stretching the
 * Component inside the cell unless the component uses filling.
 * <p>
 * If you'd like some simple rules of thumbs, use these:
 * <ul>
 * <li>Assign a weight of 1 to rows or columns that you want to
 * stretch (Also set the "f" attribute for the Components in that row
 * or column!). Other rows and columns won't.
 * <li>In some cases, you may want the table to fill while the
 * components inside it don't. For example, you may want a JPanel's
 * TitledBorder to fill the available space while the components in
 * the JPanel stay at their preferred sizes. The solution is to fill
 * the table, but set the component at the highest row and column
 * position to have row and column weights of 1. Use "nw" position on
 * all components in the last row or column. There are alternate
 * solutions using nested layouts.
 * </ul>
 * <p>
 * Ok, here are the dirty details.
 * <p>
 * If the available size is greater than the table's preferred size
 * and table filling is enabled, weighting is used (they are otherwise
 * ignored).
 * <p>
 * Weights are obtained by looking at each row and column and locating
 * the largest weight; this becomes the row or column weight. If all
 * weights are 0, we treat them as though they are all 1. We create
 * a sum for all row weights and one for all column weights. This
 * number defines the number of units into which the excess space will
 * be divided.
 * <p>
 * For example, with three column weights of 1, 1, and 1, the space is
 * divided into 3 units. If the excess space is 30 pixels, each unit
 * is 10 pixels, which is the extra space each column receives. If the
 * column weights were 0, 2, and 1, the space is still divided into 3
 * units. But the column weights specify how many units each column
 * receives. So, column 0 will receive nothing, column 1, 20 pixels
 * and column 2, 10 pixels.
 * <p>
 * Keep in mind that rows and columns are handled separately. One may
 * need filling and the other not.
 * <p>
 * When we don't have enough space for the preferred row or column
 * sizes, we ignore the user-defined weights and treat each row or
 * column as having equal weight. The approach then, is as above
 * except that we are reducing cell sizes. Another difference is that
 * no cell will be made smaller than its minimum size.
 * <h3>Special Spanning Issues</h3>
 * There are some special issues with row and column spanning. When
 * determining minimum or preferred sizes, we need to know what
 * portion of the Component's size to assign to each row and column
 * that it spans. We solve this by doing two passes. In the first, we
 * ignore spanning cells and determine row and column sizes without
 * them. In the second pass, we look to see if the spanning Component
 * will fit within the row and column sizes we determined. If not, we
 * currently distribute the extra space based on the row or column
 * weights of the rows or columns spanned. Someday, we may need to add
 * attributes to provide more control.
 * <p>
 * The row and column weights given are applied to the row or column
 * in which the Component begins.
 * <p>
 * <h3>Summary</h3>
 * <p>
 * This table summarizes the attribute information:
 * <p>
 * <table border="1" cellpadding="3" cellspacing="0">
 * <tr bgcolor="#CCCCFF" id="TableHeadingColor">
 * <td><b>Name</b></td>
 * <td><b>Description</b></td>
 * <td><b>Has Value?</b></td>
 * <td><b>Default</b></td>
 * <td><b>Scope</b></td>
 * </tr>
 * <tr bgcolor="white">
 * <td>cols</td>
 * <td>Number of columns</td>
 * <td>Yes</td>
 * <td>1</td>
 * <td>Table</td>
 * </tr>
 * <tr bgcolor="white">
 * <td>col</td>
 * <td>Place Component in this column</td>
 * <td>Yes</td>
 * <td>Next empty column</td>
 * <td>Component</td>
 * </tr>
 * <tr bgcolor="white">
 * <td>skip</td>
 * <td>Skip a number of columns</td>
 * <td>Yes</td>
 * <td>0</td>
 * <td>Component</td>
 * </tr>
 * <tr bgcolor="white">
 * <td>rspan, cspan</td>
 * <td>Row and column spanning</td>
 * <td>Yes</td>
 * <td>1</td>
 * <td>Component</td>
 * </tr>
 * <tr bgcolor="white">
 * <td>titop, tibottom, tileft, tiright</td>
 * <td>Table insets</td>
 * <td>Yes</td>
 * <td>0</td>
 * <td>Table</td>
 * </tr>
 * <tr bgcolor="white">
 * <td>rgap, cgap</td>
 * <td>Row and column gaps</td>
 * <td>Yes</td>
 * <td>0</td>
 * <td>Table</td>
 * </tr>
 * <tr bgcolor="white">
 * <td>itop, ibottom, ileft, iright</td>
 * <td>Component insets</td>
 * <td>Yes</td>
 * <td>0</td>
 * <td>Table/Component</td>
 * </tr>
 * <tr bgcolor="white">
 * <td>tn, tne, te, tse, ts, tsw, tw, tnw, tc, tf, tfh, tfv</td>
 * <td>Table placement and fill</td>
 * <td>No</td>
 * <td>tf</td>
 * <td>Table</td>
 * </tr>
 * <tr bgcolor="white">
 * <td>n, ne, e, se, s, sw, w, nw, c, f, fh, fv</td>
 * <td>Component placement and fill</td>
 * <td>No</td>
 * <td>f</td>
 * <td>Table/Component</td>
 * </tr>
 * <tr bgcolor="white">
 * <td>rweight, cweight</td>
 * <td>Row and column weights</td>
 * <td>Yes</td>
 * <td>0</td>
 * <td>Table/Component</td>
 * </tr>
 * </table>
 *
 * @author Antonio Freixas
 */

// Copyright 2000-2004 Credence Systems Corporation.
// All Rights Reserved.

public class TableLayout
    implements LayoutManager2
{

//**********************************************************************
// Private Members
//**********************************************************************

// This is the set of attributes applied to the table. Some attributes
// are used as cell defaults. Cell-only attributes are ignored

private Attributes tableAttributes;

// Attributes for each component can be found in these hash tables

private HashMap compAttributes = new HashMap();

// These variables store information about the row/col arrangement of
// the components. These are set by placeComponents()

private int nRows = 0;
private int nCols = 0;
private Component[][] components = null;

// We cache measureComponents() information so that it is recalculated
// after invalidateLayout() is called

private boolean useCacheMeasureResults = false;

// These variables store sizing information set by measureComponents()

private int[] minWidth;
private int[] prefWidth;
private int[] maxWidth;
private int[] adjWidth;
private int[] colWeight;

private int[] minHeight;
private int[] prefHeight;
private int[] maxHeight;
private int[] adjHeight;
private int[] rowWeight;

// These sizes are the minimum width for the table, not including
// either the container's insets or the table's insets

private int MinWidth	= 0;
private int MinHeight	= 0;
private int PrefWidth	= 0;
private int PrefHeight	= 0;
private int MaxWidth	= 0;
private int MaxHeight	= 0;
private int ColWeight	= 0;
private int RowWeight	= 0;

static private int classCount = 0;
private int instanceCount = classCount++;

//**********************************************************************
// Constructors
//**********************************************************************

/**
 * Construct a new TableLayout.
 */

public
TableLayout()
{
    this(null);
}

/**
 * Construct a new TableLayout with the given attributes.
 *
 * @param attributes A list of attributes for the table. The list is
 * 	described in the class documentation above. Cell-only
 * 	attributes are ignored.
 */

public
TableLayout(
    String attributes)
{
    tableAttributes = new Attributes(attributes);
}

//**********************************************************************
// Public
//**********************************************************************

/**
 * Reset the table attributes for the layout.
 *
 * @param attributes The new table attributes.
 */

public void
setTableAttributes(
    String attributes)
{
    // Set the attributes for the table

    tableAttributes = new Attributes(attributes);

    // Since the component attributes "inherit" from the table
    // attributes, any change to the table attributes causes us to
    // reprocess all existing component attributes

    Iterator iter = compAttributes.keySet().iterator();
    while (iter.hasNext()) {
	Component comp = (Component)iter.next();
	Attributes a = (Attributes)compAttributes.get(comp);
	a.parse();			// Re-parse
    }

    components = null;
    useCacheMeasureResults = false;
}

/**
 * Reset the attributes for a component in the layout. The component
 * must already have been added to the container or else this call has
 * no effect.
 *
 * @param comp The component to alter.
 * @param attributes The new attributes for the component.
 */

public void
setAttributes(
    Component comp,
    String attributes)
{
    if (compAttributes.get(comp) != null) {
	Attributes a = new Attributes(attributes, false);
	compAttributes.put(comp, a);

	components = null;
	useCacheMeasureResults = false;
    }
}

/**
 * Adds the component with the specified attributes to the layout.
 *
 * @param attributes A list of attributes for the component. The list
 * 	is described in the class documentation above. Table-only
 * 	attributes are ignored.
 * @param comp The component to be added.
 */

public void
addLayoutComponent(
    String attributes,
    Component comp)
{
    Attributes a = new Attributes(attributes, false);
    compAttributes.put(comp, a);

    components = null;
    useCacheMeasureResults = false;

    // DEBUG

//     if ("DEBUG".equals(comp.getName())) {
// 	System.out.println(
// 	    "Adding comp " + comp.getClass().getName() + " " + a);
//     }
}

/**
 * Adds the specified component to the layout, using the specified
 * constraint object (which we expect to be a String of attributes).
 *
 * @param comp The component to be added.
 * @param constraints  A list of attributes for the component. The list
 * 	is described in the class documentation above. Table-only
 * 	attributes are ignored.
 */

public void
addLayoutComponent(
    Component comp,
    Object constraints)
{
    String attributes = "";
    if (constraints instanceof String) {
	attributes = (String)constraints;
    }
    addLayoutComponent(attributes, comp);
}

/**
 * Removes the specified component from the layout.

 * @param comp The component to be removed.
 */

public void
removeLayoutComponent(
    Component comp)
{
    compAttributes.remove(comp);
    components = null;
}

/**
 * Calculates the minimum size dimensions for the layout given the
 * components in the a parent container.
 *
 * @param parent The container to be laid out.
 * @return The minimum layout size.
 * @see #preferredLayoutSize(Container)
 * @see #maximumLayoutSize(Container)
 */

public Dimension
minimumLayoutSize(
    Container parent)
{
    Insets insets = parent.getInsets();
    measureComponents(parent);
    int w =
	insets.left + insets.right +
	tableAttributes.tableInsets.left +
	tableAttributes.tableInsets.right +
	MinWidth;
    int h =
	insets.top + insets.bottom +
	tableAttributes.tableInsets.top +
	tableAttributes.tableInsets.bottom +
	MinHeight;
    if (w > Short.MAX_VALUE) w = Short.MAX_VALUE;
    if (h > Short.MAX_VALUE) h = Short.MAX_VALUE;

    return new Dimension(w, h);
}

/**
 * Calculates the preferred size dimensions for the layout given the
 * components in a parent container.
 *
 * @param parent The container to be laid out.
 * @return The preferred layout size.
 * @see #minimumLayoutSize(Container)
 * @see #maximumLayoutSize(Container)
 */

public Dimension
preferredLayoutSize(
    Container parent)
{
    Insets insets = parent.getInsets();
    measureComponents(parent);
    int w =
	insets.left + insets.right +
	tableAttributes.tableInsets.left +
	tableAttributes.tableInsets.right +
	PrefWidth;
    int h =
	insets.top + insets.bottom +
	tableAttributes.tableInsets.top +
	tableAttributes.tableInsets.bottom +
	PrefHeight;
    if (w > Short.MAX_VALUE) w = Short.MAX_VALUE;
    if (h > Short.MAX_VALUE) h = Short.MAX_VALUE;

    return new Dimension(w, h);
}

/**
 * Calculates the maximum size dimensions for the layout given the
 * components in a parent container.
 *
 * @param parent The container parent.
 * @return The maximum layout size.
 * @see #minimumLayoutSize(Container)
 * @see #preferredLayoutSize(Container)
 */

public Dimension
maximumLayoutSize(
    Container parent)
{
    Insets insets = parent.getInsets();
    measureComponents(parent);

    // Note that the maximum size of the container is not the maximum
    // size of the table if the fill options are not used

    int w = Short.MAX_VALUE;
    if (tableAttributes.tableHorizontal != Attributes.FILL) {
	w = insets.left + insets.right +
	    tableAttributes.tableInsets.left +
	    tableAttributes.tableInsets.right +
	    MaxWidth;
	if (w > Short.MAX_VALUE) w = Short.MAX_VALUE;
    }

    int h = Short.MAX_VALUE;
    if (tableAttributes.tableVertical != Attributes.FILL) {
	h = insets.top + insets.bottom +
	    tableAttributes.tableInsets.top +
	    tableAttributes.tableInsets.bottom +
	    MaxHeight;
	if (h > Short.MAX_VALUE) h = Short.MAX_VALUE;
    }

    return new Dimension(w, h);
}

/**
 * Returns the alignment along the x axis. This always returns 0.5.
 *
 * @param parent The container whose alignment we want.
 * @return The alignment along the x axis.
 */

public float
getLayoutAlignmentX(
    Container parent)
{
    return 0.5f;
}

/**
 * Returns the alignment along the y axis. This always returns 0.5.
 *
 * @param parent The container whose alignment we want.
 * @return The alignment along the y axis.
 */

public float
getLayoutAlignmentY(
    Container parent)
{
    return 0.5f;
}

/**
 * Invalidates the layout. Cached information will be discarded.
 *
 * @param parent The container whose alignment we want.
 */

public void
invalidateLayout(
    Container parent)
{
    useCacheMeasureResults = false;
}

/**
 * Lays out the components in the given container.
 *
 * @param parent The container which needs to be laid out.
 */

public void
layoutContainer(
    Container parent)
{
    // Get the row and column measurements

    measureComponents(parent);

    // Get the parent insets and determine the full amount of space we
    // have available

    Insets insets = parent.getInsets();
    int fullWidth =
	parent.getSize().width -
	(insets.left + insets.right) -
	(tableAttributes.tableInsets.left +
	 tableAttributes.tableInsets.right);
    int fullHeight = parent.getSize().height -
	(insets.top + insets.bottom) -
	(tableAttributes.tableInsets.top +
	 tableAttributes.tableInsets.bottom);

    // We normally draw each row and column in its preferred size. If
    // we have more space, we grow the cells. If less, we shrink the
    // cells

    boolean shrinkWidth = fullWidth < PrefWidth;
    boolean shrinkHeight = fullHeight < PrefHeight;

    // Get the position and size of the table. There are three
    // possibilities for the table size:
    //
    //   * The available space equals or exceeds the preferred size
    //     and the table is filled - use the full space available. We
    //     will expand the cells in a later step.
    //
    //   * The available space equals or exceeds the preferred size
    //     and the table is not filled - use the preferred size.
    //
    //   * The available space is less than the preferred size - use
    //     the available space. We will shrink the cells in a later
    //     step.

    int tableX = insets.left + tableAttributes.tableInsets.left;
    int tableY = insets.top + tableAttributes.tableInsets.top;

    int tableWidth = PrefWidth;
    if (shrinkWidth ||
	tableAttributes.tableHorizontal == Attributes.FILL) {
	tableWidth = fullWidth;
    }
    int tableHeight = PrefHeight;
    if (shrinkHeight ||
	tableAttributes.tableVertical == Attributes.FILL) {
	tableHeight = fullHeight;
    }

    if (tableAttributes.tableHorizontal == Attributes.CENTER ||
	tableAttributes.tableHorizontal == Attributes.FILL) {
	tableX += (fullWidth - tableWidth) / 2;
    }
    else if (tableAttributes.tableHorizontal == Attributes.RIGHT) {
	tableX += fullWidth - tableWidth;
    }

    if (tableAttributes.tableVertical == Attributes.CENTER ||
	tableAttributes.tableVertical == Attributes.FILL) {
	tableY += (fullHeight - tableHeight) / 2;
    }
    else if (tableAttributes.tableVertical == Attributes.BOTTOM) {
	tableY += fullHeight - tableHeight;
    }

    // Now adjust the column and row cell sizes

    adjustCellSizes(
	nCols,
	(tableAttributes.tableHorizontal == Attributes.FILL),
	shrinkWidth,
	minWidth,
	PrefWidth, prefWidth,
	fullWidth,
	ColWeight, colWeight,
	adjWidth,
	parent);

    adjustCellSizes(
	nRows,
	(tableAttributes.tableVertical == Attributes.FILL),
	shrinkHeight,
	minHeight,
	PrefHeight, prefHeight,
	fullHeight,
	RowWeight, rowWeight,
	adjHeight,
	parent);

    // Begin the  component layout loop

    for (int r = 0; r < nRows; r++) {
	for (int c = 0; c < nCols; c++) {
	    Component comp = components[r][c];
	    if (comp == null) continue;

	    Attributes attributes = (Attributes)compAttributes.get(comp);

	    Dimension compMinSize = comp.getMinimumSize();
	    Dimension compPrefSize = comp.getPreferredSize();
	    Dimension compMaxSize = comp.getMaximumSize();

	    // Base position

	    int compX =
		tableX +
		(tableAttributes.cGap * c) +
		attributes.cellInsets.left;
	    for (int i = 0; i < c; i++) compX += adjWidth[i];

	    int compY =
		tableY +
		(tableAttributes.rGap * r) +
		attributes.cellInsets.top;
	    for (int i = 0; i < r; i++) compY += adjHeight[i];

	    // Get the cell size. This has to take into account row
	    // and column spanning

	    int cellWidth = adjWidth[c];
	    for (int i = 1; i < attributes.cSpan; i++) {
		cellWidth += tableAttributes.cGap + adjWidth[c + i];
	    }

	    int cellHeight = adjHeight[r];
	    for (int i = 1; i < attributes.rSpan; i++) {
		cellHeight += tableAttributes.rGap + adjHeight[r + i];
	    }

	    int insetCellWidth =
		cellWidth -
		attributes.cellInsets.left - attributes.cellInsets.right;
	    int insetCellHeight =
		cellHeight -
		attributes.cellInsets.top - attributes.cellInsets.bottom;

	    // Get the component size. Use the preferred size, if
	    // possible. If not use the cell size minus insets

	    int compWidth = compPrefSize.width;
	    int compHeight = compPrefSize.height;
	    if (compWidth > insetCellWidth) compWidth = insetCellWidth;
	    if (compHeight > insetCellHeight) compHeight = insetCellHeight;

	    // Adjust for fill

	    if (attributes.horizontal == Attributes.FILL) {
		compWidth = insetCellWidth;
		compWidth = Math.max(compWidth, compMinSize.width);
		// Some components (like JButton) can exceed their max size
		// compWidth = Math.min(compWidth, compMaxSize.width);
	    }
	    if (attributes.vertical == Attributes.FILL) {
		compHeight = insetCellHeight;
		compHeight = Math.max(compHeight, compMinSize.height);
		// Some components (like JButton) can exceed their max size
		// compHeight = Math.min(compHeight, compMaxSize.height);
	    }

	    // Position properly. We treat FILL like CENTER since
	    // the min/max limits may have prevented us from really
	    // filling

	    if (attributes.horizontal == Attributes.CENTER ||
		attributes.horizontal == Attributes.FILL) {
		compX += (cellWidth -
			  (attributes.cellInsets.left +
			   attributes.cellInsets.right) - compWidth) / 2;
	    }
	    else if (attributes.horizontal == Attributes.RIGHT) {
		compX += (cellWidth -
			  (attributes.cellInsets.left +
			   attributes.cellInsets.right) - compWidth);
	    }

	    if (attributes.vertical == Attributes.CENTER ||
		attributes.vertical == Attributes.FILL) {
		compY += (cellHeight -
			  (attributes.cellInsets.top +
			   attributes.cellInsets.bottom) - compHeight) / 2;
	    }
	    else if (attributes.vertical == Attributes.BOTTOM) {
		compY += (cellHeight -
			  (attributes.cellInsets.top +
			   attributes.cellInsets.bottom) - compHeight);
	    }

	    // Place the component

	    comp.setBounds(compX, compY, compWidth, compHeight);

	    // DEBUG

// 	    if ("DEBUG".equals(parent.getName())) {
// 		System.out.println("Placing component " +
// 				   comp.getClass().getName() +
// 				   " (" + compX + ", " + compY + ") " +
// 				   compWidth + " x " + compHeight);
// 	    }
	}
    }
}

// The inherited toString() method is acceptable.

//**********************************************************************
// Private
//**********************************************************************

/**
 * The row or column sizes need to be adjusted. We may want to grow
 * or shrink the sizes, based on whether the available space is larger
 * or smaller than the preferred size.
 * <p>
 * If we grow the table, we pay attention to the user's weighting
 * factors. If we shrink the table, we assign all cells a weight
 * factor of 1.
 *
 * @param nCells The number of cells in the row or column.
 * @param fill True if the table rows or columns should fill the
 * 	available space.
 * @param shrink True if the available size is less than the preferred
 *	size.
 * @param minSize The minimum sizes of each row or column.
 * @param PrefSize The sum of the preferred sizes of all cells in the
 * 	row or column plus any cell gaps.
 * @param prefSize The preferred sizes of each row or column.
 * @param CellWeight The sum of all cell weights in the row or column.
 * @param cellWeight The weight of each row or column.
 * @param adjSize The adjusted size of each row or column. The
 * 	contents of this array are set and returned.
 */

private void
adjustCellSizes(
    int nCells,
    boolean fill,
    boolean shrink,
    int[] minSize,
    int PrefSize,
    int[] prefSize,
    int fullSize,
    int CellWeight,
    int[] cellWeight,
    int[] adjSize,
    Container parent)

{
    // The sum of the weights (CellWeight) determines how many units
    // any excess (or reduced) space should be divided into. The
    // unitOfSpace variable is the size of each unit.
    //
    // We use weighting under two conditions:
    //
    //   * We are shrinking the table.
    //
    //   * We have more space than we need and the user asked us to
    //     fill the available space
    //
    // When we have more than enough space for the preferred row and
    // column sizes, we follow the user's weighting. There is a
    // special case if all weights are 0: the weights are treated as
    // thought they were all 1.
    //
    // When do not have enough space, we weight everything the same.

    double unitOfSpace = 0.0;
    if (shrink || fill) {
	unitOfSpace =
	    (double)(fullSize - PrefSize) /
	    (double)((shrink || CellWeight == 0) ? nCells : CellWeight);
    }
    else {
	// No adjustment needed: use the preferred sizes

	for (int i = 0; i < nCells; i++) {
	    adjSize[i] = prefSize[i];
	}
	return;
    }

    double extraSpace;	int iExtraSpace;
    double error = 0.0;	int iError = -999999;

    int adjWeight;

    for (int i = 0; i < nCells; i++) {

	// Initialize the adjusted size to the preferred size

	adjSize[i] = prefSize[i];

	// Get the cell weight based on various conditions

	adjWeight = (shrink || CellWeight == 0) ? 1 : cellWeight[i];

	// Determine how much extra space to give each cell. The space
	// is the weight (number of units) times the unit size. We can
	// only assign an integer number of pixels, which creates a
	// fractional error

	extraSpace = unitOfSpace * adjWeight;
	iExtraSpace = (int)extraSpace;
	adjSize[i] += iExtraSpace;

	error += extraSpace - iExtraSpace;
	iError = (int)error;

	// Increment/decrement this cell by the accumulated integer
	// error, if it's not 0

	if (shrink) {
	    if (iError < 0) {
		adjSize[i] += iError;
	    }
	}
	else {
	    if (iError > 0) {
		adjSize[i] += iError;
	    }
	}
	error -= iError;

	// If we're shrinking, we need to prevent any cell from
	// shrinking below its minimum size. The error is adjusted to
	// include the space added to the cell

	if (shrink && (adjSize[i] < minSize[i])) {
	    error -= minSize[i] - adjSize[i];
	    adjSize[i] = minSize[i];
	}

	iError = (int)error;
    }

    // If we are growing, the error should be less than 1 pixel. If we
    // are shrinking, we limit each cell to its minimum size, so we
    // can accumulate larger errors as cells refuse to shrink. So we
    // distribute the error to cells that can still shrink. We repeat
    // this until we've reduced the error to 0 or we're unable to
    // shrink the error anymore.
    //
    // Remember that iError is a negative number

    if (shrink || iError < 0) {
	int lastIError;

	do {
	    lastIError = iError;
	    for (int i = 0; i < nCells; i++) {

		// If the cell is already at its minimum size, skip it

		if (adjSize[i] > minSize[i]) {
		    adjSize[i]--;
		    iError++;
		}
	    }
	}
	while (iError < 0 && iError > lastIError);
    }

    // DEBUG

//     if ("DEBUG".equals(parent.getName())) {
// 	System.out.println("  iError " + iError);
// 	for (int i = 0; i < nCells; i++) {
// 	    System.out.println("  " + i + ") Adjusted size = " + adjSize[i]);
// 	}
//     }
}

/**
 * For each component, determine its row/col position and place it in
 * an array for easy access later. Elements spanning multiple rows
 * and/or columns are placed in the NW row/col slot in the array.
 * Results are placed in class fields.
 *
 * @param parent The parent container.
 */

private void
placeComponents(
    Container parent)
{
    // If we haven't added or removed a component since the last time
    // placeComponents() was called, we assume the current results are
    // OK

    if (components != null) return;

    int compCount = parent.getComponentCount();

    // Get the number of columns specified by the user

    nCols = tableAttributes.columns;

    // Create the array of components

    CompArray compArray = new CompArray(tableAttributes.columns, compCount);

    // Fill the array with components, taking row/column spanning
    // into account

    int row = 0;
    int col = 0;

    for (int i = 0; i < compCount; i++) {

	// Get the next component and its options

	Component comp = parent.getComponent(i);
	Attributes attributes = (Attributes)compAttributes.get(comp);

	// If the column span is greater than the column size,
	// truncate it to the column size

	attributes.cSpan = attributes.originalCSpan;
	if (attributes.cSpan > tableAttributes.columns) {
	    attributes.cSpan = tableAttributes.columns;

	}
	// Handle options to force us to column 0 or to skip columns

	if (attributes.column != Attributes.NEXT_COLUMN) {
	    if (col > attributes.column) row++;
	    col = attributes.column;
	}
	col += attributes.skip;
	if (col >= nCols) {
	    row++;
	    col = 0;
	}

	// Skip over any cells that are already occupied

	while (compArray.get(row, col) != null) {
	    col++;
	    if (col >= nCols) {
		row++;
		col = 0;
	    }
	}

	// If spanning multiple columns, will we fit on this row?

	if (col + attributes.cSpan > nCols) {
	    row++;
	    col = 0;
	}

	// For now, fill all the cells that are occupied by this
	// component

	for (int c = 0; c < attributes.cSpan; c++) {
	    for (int r = 0; r < attributes.rSpan; r++) {
		compArray.set(row + r, col + c, comp);
	    }
	}

	// Advance to the next cell, ready for the next component

	col += attributes.cSpan;
	if (col >= nCols) {
	    row++;
	    col = 0;
	}
    }

    // Now we know how many rows there are. We can use a normal,
    // properly sized array from now on. The array returned includes
    // the maximum row into which anything was entered, including any
    // row spans

    components = compArray.getArray();
    nRows = components.length;

    // Now we've positioned our components we can thin out the cells so
    // we only remember the top left corner of each component

    for (row = 0; row < nRows; row++) {
	for (col = 0; col < nCols; col++) {
	    Component comp = components[row][col];
	    for (int r = row; r < nRows && components[r][col] == comp; r++) {
		for (int c = col; c < nCols && components[r][c] == comp; c++) {
		    if (r > row || c > col) {
			components[r][c] = null;
		    }
		}
	    }
	}
    }

    // DEBUG

//     if ("DEBUG".equals(parent.getName())) {
// 	System.out.println("placeComponents finished: rows = " +
// 			   nRows + " cols = " + nCols);
// 	for (int r = 0; r < nRows; r++) {
// 	    System.out.println("Row " + r + ":");
// 	    for (int c = 0; c < nCols; c++) {
// 		System.out.println("  Col " + c + " Comp " +
// 				   ((components[r][c] == null) ?
// 				    "none" :
// 				    components[r][c].getClass().getName()));
// 	    }
// 	}
//     }
}

/**
 * In this method, we will determine the minimum, preferred and
 * maximum sizes of the components as layed out by the table layout
 * manager
 *
 * @param parent The parent container.
 */

private void
measureComponents(
    Container parent)
{
    if (useCacheMeasureResults) return;

    // Determine the row/col positions for the components

    placeComponents(parent);

    // Allocate new arrays to store row and column preferred and min
    // sizes, but only if the old arrays aren't big enough

    if (minWidth == null || minWidth.length < nCols) {
	minWidth = new int[nCols];
	prefWidth = new int[nCols];
	maxWidth = new int[nCols];
	adjWidth = new int[nCols];
	colWeight = new int[nCols];
    }
    if (minHeight == null || minHeight.length < nRows) {
	minHeight = new int[nRows];
	prefHeight = new int[nRows];
	maxHeight = new int[nRows];
	adjHeight = new int[nRows];
	rowWeight = new int[nRows];
    }

    for (int i = 0; i < nCols; i++) {
	minWidth[i] = 0;
	prefWidth[i] = 0;
	maxWidth[i] = 0;
	colWeight[i] = 0;
    }
    for (int i = 0; i < nRows; i++) {
	minHeight[i] = 0;
	prefHeight[i] = 0;
	maxHeight[i] = 0;
	rowWeight[i] = 0;
    }

    // Measure the minimum and preferred size of each row and column

    for (int row = 0; row < nRows; row++) {
	for (int col = 0; col < nCols; col++) {
	    Component comp = components[row][col];
	    if (comp != null) {
		Attributes attributes = (Attributes)compAttributes.get(comp);

		Dimension minSize = new Dimension(comp.getMinimumSize());
		Dimension prefSize = new Dimension(comp.getPreferredSize());
		Dimension maxSize = new Dimension(comp.getMaximumSize());

		// Add the cell insets

		minSize.width +=
		    attributes.cellInsets.left + attributes.cellInsets.right;
		minSize.height +=
		    attributes.cellInsets.top + attributes.cellInsets.bottom;
		prefSize.width +=
		    attributes.cellInsets.left + attributes.cellInsets.right;
		prefSize.height +=
		    attributes.cellInsets.top + attributes.cellInsets.bottom;
		maxSize.width +=
		    attributes.cellInsets.left + attributes.cellInsets.right;
		maxSize.height +=
		    attributes.cellInsets.right + attributes.cellInsets.bottom;

		// Make sure that 0 <= minSize <= prefSize <= maxSize

		limitDimension(minSize, new Dimension(0, 0));
		limitDimension(prefSize, minSize);
		limitDimension(maxSize, prefSize);

		// First pass, we determine the sizes while ignoring
		// components which span columns or rows

		if (attributes.cSpan == 1) {
		    minWidth[col] =
			Math.max(minSize.width, minWidth[col]);
		    prefWidth[col] =
			Math.max(prefSize.width, prefWidth[col]);
		    maxWidth[col] =
			Math.max(maxSize.width, maxWidth[col]);
		}

		if (attributes.rSpan == 1) {
		    minHeight[row] =
			Math.max(minSize.height, minHeight[row]);
		    prefHeight[row] =
			Math.max(prefSize.height, prefHeight[row]);
		    maxHeight[row] =
			Math.max(maxSize.height, maxHeight[row]);
		}

		// Get the row and column weights. The weight is the
		// maximum value for the row or column

		if (attributes.cWeight > colWeight[col]) {
		    colWeight[col] = attributes.cWeight;
		}
		if (attributes.rWeight > rowWeight[row]) {
		    rowWeight[row] = attributes.rWeight;
		}
	    }
	}
    }

    // Do it again, but just for components which span multiple cells.
    //

    for (int row = 0; row < nRows; row++) {
	for (int col = 0; col < nCols; col++) {
	    Component comp = components[row][col];
	    if (comp != null) {
		Attributes attributes = (Attributes)compAttributes.get(comp);

		if (attributes.rSpan == 1 && attributes.cSpan == 1) continue;

		Dimension minSize = new Dimension(comp.getMinimumSize());
		Dimension prefSize = new Dimension(comp.getPreferredSize());
		Dimension maxSize = new Dimension(comp.getMaximumSize());

		// Add the cell insets

		minSize.width +=
		    attributes.cellInsets.top + attributes.cellInsets.bottom;
		minSize.height +=
		    attributes.cellInsets.left + attributes.cellInsets.right;
		prefSize.width +=
		    attributes.cellInsets.top + attributes.cellInsets.bottom;
		prefSize.height +=
		    attributes.cellInsets.left + attributes.cellInsets.right;
		maxSize.width +=
		    attributes.cellInsets.top + attributes.cellInsets.bottom;
		maxSize.height +=
		    attributes.cellInsets.left + attributes.cellInsets.right;

		// Make sure that 0 <= minSize <= prefSize <= maxSize

		limitDimension(minSize, new Dimension(0, 0));
		limitDimension(prefSize, minSize);
		limitDimension(maxSize, prefSize);

		if (attributes.cSpan > 1) {
		    adjustForSpans(col,
				   minSize.width,
				   minWidth,
				   colWeight,
				   attributes.cSpan,
				   tableAttributes.cGap);
		    adjustForSpans(col,
				   prefSize.width,
				   prefWidth,
				   colWeight,
				   attributes.cSpan,
				   tableAttributes.cGap);
		    adjustForSpans(col,
				   maxSize.width,
				   maxWidth,
				   colWeight,
				   attributes.cSpan,
				   tableAttributes.cGap);
		}

		if (attributes.rSpan > 1) {
		    adjustForSpans(row,
				   minSize.height,
				   minHeight,
				   rowWeight,
				   attributes.rSpan,
				   tableAttributes.rGap);
		    adjustForSpans(row,
				   prefSize.height,
				   prefHeight,
				   rowWeight,
				   attributes.rSpan,
				   tableAttributes.rGap);
		    adjustForSpans(row,
				   maxSize.height,
				   maxHeight,
				   rowWeight,
				   attributes.rSpan,
				   tableAttributes.rGap);
		}
	    }
	}
    }

    // Add up all the individual values

    MinWidth = 0;
    MinHeight = 0;
    PrefWidth = 0;
    PrefHeight = 0;
    MaxWidth = 0;
    MaxHeight = 0;
    ColWeight = 0;
    RowWeight = 0;

    // Sum up everything

    for (int i = 0; i < nCols; i++) {
	MinWidth += minWidth[i];
	PrefWidth += prefWidth[i];
	MaxWidth += maxWidth[i];
	ColWeight += colWeight[i];
    }

    for (int i = 0; i < nRows; i++) {
	MinHeight += minHeight[i];
	PrefHeight += prefHeight[i];
	MaxHeight += maxHeight[i];
	RowWeight += rowWeight[i];
    }

    // Add in the table gaps

    int cExtra = tableAttributes.cGap * (nCols - 1);
    int rExtra = tableAttributes.rGap * (nRows - 1);

    MinWidth += cExtra;
    PrefWidth += cExtra;
    MaxWidth += cExtra;

    MinHeight += rExtra;
    PrefHeight += rExtra;
    MaxHeight += rExtra;

    // DEBUG

//     if ("DEBUG".equals(parent.getName())) {
// 	System.out.println("MeasureComponents:");
// 	System.out.println("  Min " + MinWidth  + ", " + MinHeight);
// 	System.out.println("  Pref " + PrefWidth + ", " + PrefHeight);
// 	System.out.println("  Max " + MaxWidth  + ", " + MaxHeight);
// 	System.out.println("  Weight " + ColWeight  + ", " + RowWeight );

// 	for (int c = 0; c < nCols; c++) {
// 	    System.out.println("    Col " + c +
// 			       " min " + minWidth[c] +
// 			       " pref " + prefWidth[c] +
// 			       " max " + maxWidth[c] +
// 			       " wgt " + colWeight[c]);
// 	}
// 	for (int r = 0; r < nRows; r++) {
// 	    System.out.println("    Row " + r +
// 			       " min " + minHeight[r] +
// 			       " pref " + prefHeight[r] +
// 			       " max " + maxHeight[r] +
// 			       " wgt " + rowWeight[r]);
// 	}
//     }

    // We keep using these results until the layout is invalidated

    useCacheMeasureResults = true;
}

/**
 * Make sure the first dimension is greater than or equal to the
 * second. Also make sure the first dimension is less than an absolute
 * maximum.
 *
 * @param d1 The first dimension (may be modified).
 * @param d2 The second dimension (will not be modified).
 */

private void
limitDimension(
    Dimension d1,
    Dimension d2)
{
    if (d1.width < d2.width) d1.width = d2.width;
    if (d1.height < d2.height) d1.height = d2.height;
    if (d1.width > Short.MAX_VALUE) d1.width = Short.MAX_VALUE;
    if (d1.height > Short.MAX_VALUE) d1.height = Short.MAX_VALUE;
}

/**
 * If a component spans multiple rows or columns, we need to
 * distribute portions of its size to the individual rows and columns.
 *
 * @param pos Row or column position where the span component starts.
 * @param compSize The height or width of the component
 * @param sizes The array of widths or heights to adjust.
 * @param span The number of cells spanned by the component.
 * @param gap The row or column gap.
 */

private void
adjustForSpans(
    int pos,
    int compSize,
    int[] sizes,
    int[] weight,
    int span,
    int gap)
{
    // The total size is the size of the rows or columns plus all the
    // space in between

    int totalSize = 0;
    for (int i = 0; i < span; i++) {
	totalSize += sizes[pos + i];
    }
    totalSize += gap * (span - 1);

    // If the spanned component is bigger than the the rows or columns
    // it spans, we divide the extra space based on the weights of the
    // spanned rows or columns

    if (compSize > totalSize) {
	int extra = compSize - totalSize;
	int totalWeight = 0;
	for (int i = 0; i < span; i++) {
	    totalWeight += weight[pos + i];
	}
	if (totalWeight == 0) totalWeight = span;

	int remainder = extra;
	for (int i = 0; i < span; i++) {
	    int portion = (extra * weight[pos + i]) / totalWeight;
	    sizes[pos + i] += portion;
	    remainder -= portion;
	}

	// Because of truncation, we may have a little left over which
	// we give to the last row or column

	if (remainder > 0) {
	    sizes[pos + span - 1] += remainder;
	}
    }
}

//**********************************************************************
// Inner Classes
//**********************************************************************

//**********************************************************************
//
// Attributes
//
// This class converts a string attribute into a data structure.
//
//**********************************************************************

// Attributes not needing assigment

static String[] attr = {
    "tn", "tne", "tnw",
    "ts", "tse", "tsw",
    "te", "tw", "tc",
    "tfh", "tfv", "tf",
    "n", "ne", "nw",
    "s", "se", "sw",
    "e", "w", "c",
    "fh", "fv", "f",
};

// Attributes needing assigment

static String[] assgn = {
    "cols",
    "rgap",
    "cgap",
    "titop",
    "tibottom",
    "tileft",
    "tiright",
    "itop",
    "ibottom",
    "ileft",
    "iright",
    "rweight",
    "cweight",
    "rspan",
    "cspan",
    "col",
    "skip"
};

private class Attributes
{

// Constants used for fill and placement operations

static final int CENTER		= 0;
static final int LEFT		= 1;
static final int RIGHT		= 2;
static final int TOP 		= 3;
static final int BOTTOM 	= 4;
static final int FILL 		= 5;

// Constants for column placement

static final int NEXT_COLUMN	= -1;

// The attributes in their original string form

String attrString;

// Table-only options

int columns = 1;
int tableHorizontal = FILL;
int tableVertical = FILL;
int rGap = 0;
int cGap = 0;
Insets tableInsets = new Insets(0, 0, 0, 0);

// Table/cell options

int horizontal = FILL;
int vertical = FILL;
Insets cellInsets = new Insets(0, 0, 0, 0);
int rWeight = 0;
int cWeight = 0;

// Cell-only options

int rSpan = 1;
int cSpan = 1;
int originalCSpan = cSpan;
int column = NEXT_COLUMN;
int skip = 0;

int tkPos = 0;
boolean isTableAttributes = false;

Attributes(
    String attrString)
{
    this(attrString, true);
}

Attributes(
    String attrString,
    boolean isTableAttributes)
{
    // Save the string for later access

    this.attrString = attrString;

    this.isTableAttributes = isTableAttributes;

    parse();
}

public String
toString()
{
    String sep = System.getProperty("line.separator");
    return "TableLayout Attributes:" + sep +
	"isTableAttributes = " + isTableAttributes + sep +
	"columns = " + columns + sep +
	"tableHorizontal = " + tableHorizontal + " " +
	"tableVertical = " + tableVertical + sep +
	"rGap = " + rGap + " " +
	"cGap = " + cGap + sep +
	"tableInsets = " + tableInsets + sep +
	"horizontal = " + horizontal + " " +
	"vertical = " + vertical + sep +
	"cellInsets = " + cellInsets + sep +
	"rWeight = " + rWeight + " " +
	"cWeight = " + cWeight + sep +
	"rSpan = " + rSpan + " " +
	"cSpan = " + cSpan + sep +
	"originalCSpan = " + originalCSpan + sep +
	"column = " + column + sep +
	"skip = " + skip;
}

String
getStringAttributes()
{
    return attrString;
}

private char
getTokenChar()
{
    if (tkPos >= attrString.length()) return 0;
    return Character.toLowerCase(attrString.charAt(tkPos++));
}


private String
getToken()
{
    StringBuffer token = new StringBuffer();

    char c = getTokenChar();

    // Skip whitespace

    if (Character.isWhitespace(c)) {
	do {
	    c = getTokenChar();
	}
	while (Character.isWhitespace(c));
    }

    // Attributes

    if (Character.isLetter(c)) {
	do {
	    token.append(c);
	    c = getTokenChar();
	}
	while (Character.isLetter(c));
	if (c != 0) tkPos--;
    }

    // Integers

    else if (Character.isDigit(c)) {
	do {
	    token.append(c);
	    c = getTokenChar();
	}
	while (Character.isDigit(c));
	if (c != 0) tkPos--;
    }

    // End of string

    else if (c == 0) {
	return null;
    }

    // Everything else is a single-character token

    else {
	token.append(c);
    }

    return new String(token);
}

void
parse()
{
    // Initialize this set of attributes so it starts out as a copy of
    // the given default, at least for those options where the cell
    // can override a table default

    if (!isTableAttributes) {
	horizontal = tableAttributes.horizontal;
	vertical = tableAttributes.vertical;
	cellInsets = (Insets)tableAttributes.cellInsets.clone();
	rWeight = tableAttributes.rWeight;
	cWeight = tableAttributes.cWeight;
    }

    if (attrString == null) return;
    tkPos = 0;

    while (tkPos < attrString.length()) {
	parseOption();
    }

    // We have checked the syntax, now check the semantics

    if (isTableAttributes) {
	if (columns == 0) {
	    reportSemanticError("cols=0");
	}
    }
    else {
	if (rSpan == 0) {
	    reportSemanticError("rspan=0");
	}
	if (cSpan == 0) {
	    reportSemanticError("cspan=0");
	}
	if (column >= tableAttributes.columns) {
	    reportSemanticError("col=" + column +
				" (max is " +
				(tableAttributes.columns - 1) + ")");
	}
    }
}

private void
parseOption()
{
    // Get the next token

    String token = getToken();
    if (token == null) return;

    boolean attributeFound = false;
    for (int i = 0; i < attr.length; i++) {
	if (token.equals(attr[i])) {
	    parseAttribute(token);
	    return;
	}
    }

    for (int i = 0; i < assgn.length; i++) {
	if (token.equals(assgn[i])) {
	    parseAssignment(token);
	    return;
	}
    }

    reportError(token, "Unrecognized attribute");
}

private void
parseAttribute(
    String token)
{
    // Table placement and fill

    if ("tnw".equals(token) ||
	"tw".equals(token) ||
	"tsw".equals(token)) {
	tableHorizontal = LEFT;
    }

    if ("tne".equals(token) ||
	"te".equals(token) ||
	"tse".equals(token)) {
	tableHorizontal = RIGHT;
    }

    if ("tn".equals(token) ||
	"tc".equals(token) ||
	"ts".equals(token)) {
	tableHorizontal = CENTER;
    }

    if ("tf".equals(token) ||
	"tfh".equals(token)) {
	tableHorizontal = FILL;
    }

    if ("tn".equals(token) ||
	"tnw".equals(token) ||
	"tne".equals(token)) {
	tableVertical = TOP;
    }

    if ("ts".equals(token) ||
	"tsw".equals(token) ||
	"tse".equals(token)) {
	tableVertical = BOTTOM;
    }

    if ("tw".equals(token) ||
	"tc".equals(token) ||
	"te".equals(token)) {
	tableVertical = CENTER;
    }

    if ("tf".equals(token) ||
	"tfv".equals(token)) {
	tableVertical = FILL;
    }

    // Cell placement and fill

    if ("nw".equals(token) ||
	"w".equals(token) ||
	"sw".equals(token)) {
	horizontal = LEFT;
    }

    if ("ne".equals(token) ||
	"e".equals(token) ||
	"se".equals(token)) {
	horizontal = RIGHT;
    }

    if ("n".equals(token) ||
	"c".equals(token) ||
	"s".equals(token)) {
	horizontal = CENTER;
    }

    if ("f".equals(token) ||
	"fh".equals(token)) {
	horizontal = FILL;
    }

    if ("n".equals(token) ||
	"nw".equals(token) ||
	"ne".equals(token)) {
	vertical = TOP;
    }

    if ("s".equals(token) ||
	"sw".equals(token) ||
	"se".equals(token)) {
	vertical = BOTTOM;
    }

    if ("w".equals(token) ||
	"c".equals(token) ||
	"e".equals(token)) {
	vertical = CENTER;
    }

    if ("f".equals(token) ||
	"fv".equals(token)) {
	vertical = FILL;
    }
}

private void
parseAssignment(
    String token)
{
    String attr = token;

    token = getToken();
    if (token != null) {
	if ("=".equals(token)) {
	    token = getToken();
	    if (token != null) {
		int value = 0;
		try {
		    value = Integer.parseInt(token);
		}
		catch (NumberFormatException e) {
		    reportError(token, "Expected an integer");
		}

		if ("cols".equals(attr))
		    columns = value;
		else if ("rgap".equals(attr))
		    rGap = value;
		else if ("cgap".equals(attr))
		    cGap = value;
		else if ("titop".equals(attr))
		    tableInsets.top = value;
		else if ("tibottom".equals(attr))
		    tableInsets.bottom = value;
		else if ("tileft".equals(attr))
		    tableInsets.left = value;
		else if ("tiright".equals(attr))
		    tableInsets.right = value;
		else if ("itop".equals(attr))
		    cellInsets.top = value;
		else if ("ibottom".equals(attr))
		    cellInsets.bottom = value;
		else if ("ileft".equals(attr))
		    cellInsets.left = value;
		else if ("iright".equals(attr))
		    cellInsets.right = value;
		else if ("rweight".equals(attr))
		    rWeight = value;
		else if ("cweight".equals(attr))
		    cWeight = value;
		else if ("rspan".equals(attr))
		    rSpan = value;
		else if ("cspan".equals(attr))
		    originalCSpan = cSpan = value;
		else if ("col".equals(attr))
		    column = value;
		else if ("skip".equals(attr))
		    skip = value;

		return;
	    }
	}
	reportError(token, "Expected an '='");
    }
    reportError(token, "Expected an '='");
}

private void
reportError(
    String token,
    String message)
{
    throw new IllegalArgumentException(
	"TableLayout: " + message + "; near '" + token +
	"' at position " + tkPos + " in '" +
	attrString + "'");
}

private void
reportSemanticError(
    String message)
{
    throw new IllegalArgumentException(
	"TableLayout: Invalid value: " + message);
}

}

//**********************************************************************
//
// CompArray
//
// We'd like to use a 2-dimensional array to help us sort out the
// layout of the various components. But in order to create the array,
// we need to know the number of rows. In order to get the number of
// rows, we need to lay out the components. So we have problem.
//
// It is not obvious how to determine the number of rows from the
// component count, due to row and column spans. So we've created the
// CompArray class to help out. It makes its best guess at the size of
// the array. If we need additional rows, it expands the array as
// efficiently as it can. When we're done, we can ask it to create a
// correctly sized array to hold our data.
//
//**********************************************************************

private class CompArray
{

private int nCols;
private int nRows;
private int maxRow = 0;
Component[][] compArray = null;

CompArray(
    int nCols,
    int compCount)
{
    this.nRows = (compCount + (nCols - 1)) / nCols;
    this.nRows = Math.max(this.nRows, 1);
    this.nCols = nCols;

    compArray = new Component[nRows][];
    for (int i = 0; i < nRows; i++) {
	compArray[i] = new Component[nCols];
	Arrays.fill(compArray[i], null);
    }
}

Component
get(
    int row,
    int col)
{
    if (row >= nRows) resize(row + 1);
    return compArray[row][col];
}

void
set(
    int row,
    int col,
    Component comp)
{
    if (row >= nRows) resize(row + 1);
    compArray[row][col] = comp;
    maxRow = Math.max(row, maxRow);
}

Component[][]
getArray()
{
    int maxRows = maxRow + 1;
    Component[][] array = new Component[maxRows][];
    for (int r = 0; r < maxRows; r++) {
	array[r] = new Component[nCols];
	System.arraycopy(compArray[r], 0, array[r], 0, nCols);
    }

    return array;
}

private void
resize(
    int newRows)
{
    // When we exceed a threshold, bump up the size by at least 10

    if (newRows - nRows < 10) newRows = nRows + 10;

    // Create the new row array and copy the old one into it

    Component[][] newArray = new Component[newRows][];
    System.arraycopy(compArray, 0, newArray, 0, nRows);

    // Initialize each new row to nulls

    for (int i = nRows; i < newRows; i++) {
	newArray[i] = new Component[nCols];
	Arrays.fill(newArray[i], null);
    }

    compArray = newArray;
    nRows = newRows;
}

}

//**********************************************************************
// End Inner Classes
//**********************************************************************

}
