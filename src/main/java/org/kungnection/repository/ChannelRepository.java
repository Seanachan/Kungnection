package org.kungnection.repository;

import org.kungnection.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ChannelRepository 負責與資料庫中的 Channel 表互動。
 * 使用六碼頻道代碼作為主鍵（code）。
 */
public interface ChannelRepository extends JpaRepository<Channel, String> {

    /**
     * 檢查是否已存在指定的頻道代碼（避免重複）
     * 注意：由於 code 是 @Id 主鍵，使用 existsById 即可。
     *
     * @param code 六碼頻道代碼
     * @return 是否存在該代碼
     */
    boolean existsById(String code);

    // 無需額外定義 findById，JpaRepository 已內建使用主鍵查詢的方法：
    // Optional<Channel> findById(String code);
}