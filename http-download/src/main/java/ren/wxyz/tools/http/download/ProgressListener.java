package ren.wxyz.tools.http.download;

/**
 * <p>
 * 进度监听器
 * </p>
 *
 * @author wxyz
 * @datetime 2016/6/1 22:08
 */
@FunctionalInterface
public interface ProgressListener {
    /**
     * 更新进度
     * @param url URL地址
     * @param rate 进度百分值
     */
    void progress(String url, int rate);
}
