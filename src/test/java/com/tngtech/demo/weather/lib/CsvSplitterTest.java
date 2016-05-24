package com.tngtech.demo.weather.lib;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CsvSplitterTest {

    private CsvSplitter splitter = new CsvSplitter();

    @Test
    public void splitterShouldSplitAtComma() {
        assertThat(splitter.split("foo,bar,baz")).containsExactly("foo", "bar", "baz");
    }

    @Test
    public void splitterShouldIgnoreCommaInsideQuote() {
        assertThat(splitter.split("\"foo,bar\",baz")).containsExactly("\"foo,bar\"", "baz");
    }
}