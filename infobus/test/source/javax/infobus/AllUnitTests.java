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

/**
 * AllUnitTests
 * @author Andrew Selkirk
 * @version $Revision: 1.1 $
 */
public class AllUnitTests {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Main
	 * @param args Command-line arguments
	 */
	public static void main(String[] args){
		junit.textui.TestRunner.run (suite());
	} // main()

	/**
	 * Test suite
	 * @return Test suite
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite("All Infobus Tests");
		suite.addTest(ColumnNotFoundExceptionUnitTest.suite());
		suite.addTest(DataItemAddedEventUnitTest.suite());
		suite.addTest(DataItemChangeEventUnitTest.suite());
		suite.addTest(DataItemChangeListenerSupportUnitTest.suite());
		suite.addTest(DataItemChangeManagerSupportUnitTest.suite());
		suite.addTest(DataItemDeletedEventUnitTest.suite());
		suite.addTest(DataItemRevokedEventUnitTest.suite());
		suite.addTest(DataItemShapeChangedEventUnitTest.suite());
		suite.addTest(DataItemValueChangedEventUnitTest.suite());
		suite.addTest(DefaultPolicyUnitTest.suite());
		suite.addTest(DuplicateColumnExceptionUnitTest.suite());
		suite.addTest(InfoBusBeanSupportUnitTest.suite());
		suite.addTest(InfoBusDataConsumerProxyUnitTest.suite());
		suite.addTest(InfoBusDataProducerProxyUnitTest.suite());
		suite.addTest(InfoBusEventUnitTest.suite());
		suite.addTest(InfoBusItemAvailableEventUnitTest.suite());
		suite.addTest(InfoBusItemRequestedEventUnitTest.suite());
		suite.addTest(InfoBusItemRevokedEventUnitTest.suite());
		suite.addTest(InfoBusMembershipExceptionUnitTest.suite());
		suite.addTest(InfoBusMemberSupportUnitTest.suite());
		suite.addTest(InfoBusUnitTest.suite());
		suite.addTest(InvalidDataExceptionUnitTest.suite());
		suite.addTest(PrioritizedDCListUnitTest.suite());
		suite.addTest(RowsetCursorMovedEventUnitTest.suite());
		suite.addTest(RowsetValidationExceptionUnitTest.suite());
		suite.addTest(StaleInfoBusExceptionUnitTest.suite());
		suite.addTest(UnsupportedOperationExceptionUnitTest.suite());
		return suite;
	} // suite()


} // AllUnitTests
