-- =============================================================================
-- SIMS Remote — Non Delivery SELECT
-- Purpose: Retrieve Non Delivery records (failed / short / undelivered lines)
-- Adjust table/column names to match your SIMS Remote schema if they differ.
-- =============================================================================

-- Parameters (SQL Server style). Swap for Oracle bind vars (:p_from_date) if needed.
DECLARE @FromDate     DATE          = DATEADD(DAY, -7, CAST(GETDATE() AS DATE));
DECLARE @ToDate       DATE          = CAST(GETDATE() AS DATE);
DECLARE @DepotCode    VARCHAR(20)   = NULL;   -- e.g. 'DBN01'; set NULL for all
DECLARE @CustomerCode VARCHAR(30)   = NULL;   -- set NULL for all
DECLARE @Principal    VARCHAR(50)   = NULL;   -- set NULL for all

-- -----------------------------------------------------------------------------
-- Main Non Delivery retrieval
-- -----------------------------------------------------------------------------
SELECT
    oh.OrderNumber,
    oh.OrderDate,
    oh.NodDate                          AS NextOrderDate,
    oh.NddDate                          AS NextDeliveryDate,
    oh.CustomerCode,
    oh.CustomerName,
    oh.DepotCode,
    oh.DepotName,
    oh.PrincipalCode,
    oh.PrincipalName,
    oh.RouteCode,
    oh.VendorCode,
    ol.LineNumber,
    ol.ProductCode,
    ol.ProductDescription,
    ol.OrderedQty,
    ISNULL(ol.DeliveredQty, 0)          AS DeliveredQty,
    (ol.OrderedQty - ISNULL(ol.DeliveredQty, 0)) AS NonDeliveredQty,
    ol.Uom,
    nd.NonDeliveryReasonCode,
    nd.NonDeliveryReasonDescription,
    nd.NonDeliveryStatus,               -- e.g. Failed / Short / Refused / No Stock
    nd.ExceptionDate,
    nd.PodReference,
    nd.CreatedBy,
    nd.CreatedAt
FROM dbo.NonDelivery nd
INNER JOIN dbo.OrderHeader oh
    ON oh.OrderId = nd.OrderId
INNER JOIN dbo.OrderLine ol
    ON ol.OrderLineId = nd.OrderLineId
WHERE nd.ExceptionDate >= @FromDate
  AND nd.ExceptionDate <  DATEADD(DAY, 1, @ToDate)
  AND (@DepotCode    IS NULL OR oh.DepotCode    = @DepotCode)
  AND (@CustomerCode IS NULL OR oh.CustomerCode = @CustomerCode)
  AND (@Principal    IS NULL OR oh.PrincipalCode = @Principal)
  AND (ol.OrderedQty - ISNULL(ol.DeliveredQty, 0)) > 0
ORDER BY
    nd.ExceptionDate DESC,
    oh.DepotCode,
    oh.OrderNumber,
    ol.LineNumber;

-- -----------------------------------------------------------------------------
-- Fallback: if SIMS Remote exposes a dedicated Non Delivery view
-- -----------------------------------------------------------------------------
-- SELECT *
-- FROM dbo.vw_SimsRemote_NonDelivery
-- WHERE ExceptionDate >= @FromDate
--   AND ExceptionDate <  DATEADD(DAY, 1, @ToDate)
-- ORDER BY ExceptionDate DESC;

-- -----------------------------------------------------------------------------
-- Fallback: derive Non Delivery from order vs delivery (no NonDelivery table)
-- -----------------------------------------------------------------------------
-- SELECT
--     oh.OrderNumber,
--     oh.CustomerCode,
--     oh.DepotCode,
--     ol.ProductCode,
--     ol.OrderedQty,
--     ISNULL(SUM(dl.DeliveredQty), 0) AS DeliveredQty,
--     ol.OrderedQty - ISNULL(SUM(dl.DeliveredQty), 0) AS NonDeliveredQty
-- FROM dbo.OrderHeader oh
-- INNER JOIN dbo.OrderLine ol
--     ON ol.OrderId = oh.OrderId
-- LEFT JOIN dbo.DeliveryLine dl
--     ON dl.OrderLineId = ol.OrderLineId
-- WHERE oh.NddDate >= @FromDate
--   AND oh.NddDate <  DATEADD(DAY, 1, @ToDate)
-- GROUP BY
--     oh.OrderNumber, oh.CustomerCode, oh.DepotCode,
--     ol.ProductCode, ol.OrderedQty
-- HAVING ol.OrderedQty - ISNULL(SUM(dl.DeliveredQty), 0) > 0;
