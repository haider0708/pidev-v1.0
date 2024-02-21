<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20240218092833 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE facture DROP FOREIGN KEY FK_FE86641071179CD6');
        $this->addSql('DROP INDEX IDX_FE86641071179CD6 ON facture');
        $this->addSql('ALTER TABLE facture DROP name_id');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE facture ADD name_id INT NOT NULL');
        $this->addSql('ALTER TABLE facture ADD CONSTRAINT FK_FE86641071179CD6 FOREIGN KEY (name_id) REFERENCES rendezvous (id)');
        $this->addSql('CREATE INDEX IDX_FE86641071179CD6 ON facture (name_id)');
    }
}
