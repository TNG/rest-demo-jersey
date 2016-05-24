package com.tngtech.demo.weather.domain.measurement;

import lombok.Builder;
import lombok.Data;

/**
 * A collected point, including some information about the range of collected values
 */

@Data
@Builder
public class DataPoint {

    public final DataPointType type;

    /**
     * the mean of the observations
     */
    public final double mean;

    /**
     * 1st quartile -- useful as a lower bound
     */
    public final int first;

    /**
     * 2nd quartile -- median value
     */
    public final int median;

    /**
     * 3rd quartile value -- less noisy upper value
     */
    public final int last;

    /**
     * the total number of measurements
     */
    public final int count;

    public final DataPointBuilder toBuilder() {
        return builder().type(type).mean(mean).first(first).median(median).last(last).count(count);
    }
}
