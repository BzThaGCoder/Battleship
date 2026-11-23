// Drop-in helpers for CreateLocationIBT — copy into the page class.
// See CreateLocationIBT.TransportViaDCWarehouse.cs.txt for full integration notes.

        /// <summary>
        /// True when the location currently selected in cboReceivingLocation is a warehouse (LocationType W).
        /// Uses the type map cached in LoadReceivingLocations; falls back to master data if needed.
        /// </summary>
        private bool IsSelectedReceivingLocationWarehouse()
        {
            int selectedLocationId;
            if (!int.TryParse(cboReceivingLocation.containedDropdownList.SelectedValue, out selectedLocationId)
                || selectedLocationId <= 0)
            {
                return false;
            }

            var typeMap = ViewState["ReceivingLocationTypes"] as Dictionary<int, string>;
            string locationType;
            if (typeMap != null && typeMap.TryGetValue(selectedLocationId, out locationType))
            {
                return string.Equals(locationType, "W", StringComparison.OrdinalIgnoreCase);
            }

            LocationCriterion criteria = new LocationCriterion { LocationID = selectedLocationId };
            LOCATION location = SIMSMasterDataController.GetLocations(criteria)?.FirstOrDefault();
            return location != null
                && string.Equals(location.LocationType, "W", StringComparison.OrdinalIgnoreCase);
        }

        /// <summary>
        /// Warehouse selected → Transport Via DC = Y and disabled (greyed out).
        /// Store / cleared selection → enabled again and defaulted to N (page default).
        /// </summary>
        private void ApplyTransportViaDCForSelectedLocation()
        {
            DropDownList ddlTransportViaDC = cboTransportViaDC.containedDropdownList;

            if (IsSelectedReceivingLocationWarehouse())
            {
                ddlTransportViaDC.SelectedValue = "Y";
                ddlTransportViaDC.Enabled = false;
            }
            else
            {
                ddlTransportViaDC.Enabled = true;
                if (ddlTransportViaDC.Items.FindByValue("N") != null)
                    ddlTransportViaDC.SelectedValue = "N";
            }

            ctrlItemScan.TransportViaDC = ddlTransportViaDC.SelectedValue == "Y";
        }
