/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package javax.infobus;

// Imports
import junit.framework.*;
import junit.extensions.*;

/**
 * InfoBusMembershipExceptionUnitTest
 * @author Andrew Selkirk
 * @version $Revision: 1.1 $
 */
public class InfoBusMembershipExceptionUnitTest extends TestCase {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Constructor
	 * @param name TODO
	 */
	public InfoBusMembershipExceptionUnitTest(String name){
		super(name);
	} // InfoBusMembershipExceptionUnitTest()

	/**
	 * Main
	 * @param args Command-line arguments
	 */
	public static void main(String[] args){
		junit.textui.TestRunner.run (suite());
	} // main()

	/**
	 * Setup test case
	 */
	protected void setUp() {
	} // setUp()

	/**
	 * Test suite
	 * @return Test suite
	 */
	public static Test suite() {
		return new TestSuite(InfoBusMembershipExceptionUnitTest.class);
	} // suite()

	//-------------------------------------------------------------
	// Test Cases -------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create exception with message and verify that the Throwable
	 * is assigned the message.
	 */
	public void testCreation() {

		// Variables
		Throwable	throwable;

		throwable = new InfoBusMembershipException("Message");
		assertTrue(throwable.getMessage().equals("Message"));

	} // testCreation()


} // InfoBusMembershipExceptionUnitTest
